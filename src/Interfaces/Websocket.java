package Interfaces;

import Games.*;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;


@ServerEndpoint(value = "/play/{gameType}/{gameID}" , configurator = HttpSessionOverride.class) //@ServerEndpoint: Der Server-Websocket hört auf die URL-Endung /play und nimmt nur darüber neue Verbindungen auf.

public class Websocket {

    /////////////////////****VARIABLES****/////////////////////

    //Statische Hashmap wird zum Speichern der Spielobjekte und dessen jeweilige GameID verwendet
    private static HashMap<Integer, Game> gameSessions = new HashMap<>();

    //Ein übergabe Objekt für fehlerhafte Spielobjekte
    private static Game errorGame;
    public HttpSessionOverride test;

    /////////////////////****PUBLIC****/////////////////////

    /**
     * Funktion OnOpen wird aufgerufen wenn sich ein neuer Websocketclient verbindet.
     * Sie verteilt die neuen Verbindungen auf die jeweiligen Spielobjekte und erstellt ggf. neue Spielobjekte.
     * @param session: die Session die sich mit dem Server verbunden hat.
     */
    @OnOpen
    public void open(@PathParam("gameType") String str_gameType,@PathParam("gameID") String str_gameID, final Session session, EndpointConfig config) throws IOException{

        HttpSession httpSession = (HttpSession) config.getUserProperties().get("sessionID");
        System.out.println("New User (SessionID:" + httpSession.getId() + ") connected with GameID:" + str_gameID + "/Type: " + str_gameType);

        session.setMaxIdleTimeout(300000);

        int gameID = Integer.parseInt(str_gameID);

        if(gameSessions.containsKey(gameID)) {
            if(!gameSessions.get(gameID).addPlayer(session)) {
                sendMsg("Error: Die Spielsession " + str_gameID + " ist bereits voll!",session);
                closeClient(session);
            }
            else {
                sendMsg("Sie sind erfolgreich der Spielsession " + gameID + " beigetreten!",session);
            }
        }
        else{
            Game newGame;

            switch(str_gameType) {
                case "ttt":
                    newGame  = new TicTacToe(gameID);
                    break;
                default:
                    sendMsg("ERROR: Spielsession konnte nicht erstellt werden da Spieltyp: " + str_gameType + " unbekannt ist!",session);
                    return;
            }

            newGame.addPlayer(session);
            gameSessions.put(gameID,newGame);
            sendMsg("Sie haben eine neue Spielsession mit der ID:" + gameID + " erstellt!",session);
        }
    }

    /**
     * Funktion OnMessage nimmt Nachrichten der Clients entgegen und verteilt diese auf die jeweilgen Spielobjekte.
     * @param message: Die jeweilige Nachricht als String.
     * @param session: Das Sessionobjekt des sendeden Clients.
     */
    @OnMessage
    public void onMessage(String message, final Session session)
    {
        String str_gameID = session.getRequestParameterMap().get("gameID").get(0);
        int gameID = parseGameID(str_gameID);

        if(gameSessions.containsKey(gameID))
        {
            if(getGameByID(gameID).isPlayerInGame(session))
            {
                System.out.println("Message from User " + session.getId() + " in Game " + gameID + ": " + message);
                getGameByID(gameID).receiveMessage(message, session);
            }
            else
            {
                //NIG-User = Non in Game User
                System.out.println("Message from NIG-User " + session.getId() + ": " + message);
            }
        }
        else
        {
            System.out.println("Message from NIG-User " + session.getId() + ": " + message);
        }
    }

    /**
     * Funktion OnOpen wird aufgerufen wenn eine Verbindung zu einem Client geschlossen oder abgebrochen wurde.
     * @param session Das Sessionobjekt des schließenden Clients.
     */
    @OnClose
    public void onClose( final Session session){
        String str_gameID = session.getRequestParameterMap().get("gameID").get(0);
        int gameID = parseGameID(str_gameID);

        if(gameSessions.containsKey(gameID))
        {
            if(getGameByID(gameID).removePlayer(session)) {
                System.out.println("User " + session.getId() + " in Game:" + gameID + " lost connection and was removed from Game!");
                if (getGameByID(gameID).getPlayerAmount() <= 0) {
                    deleteGameByID(gameID);
                }
            }
            else {
                System.out.println("User " + session.getId() + " in Game:" + gameID + " lost connection");
                System.out.println("ERROR: User " + session.getId() + " doesn't exist in Game:" + gameID + "!");
            }
        }
        else
        {
            System.out.println("User " + session.getId() + " lost connection!");
        }
    }

    /**
     * sendMsg sended eine Nachricht an einen bestimmten Client.
     * @param msg Nachricht als String.
     * @param session Das Sessionobjekt des Empfängers.
     * @return Boolean, zeigt an ob die Nachricht erfolgreich gesendet wurde.
     */
    public Boolean sendMsg(String msg, final Session session) {
        try {
            session.getBasicRemote().sendText(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /////////////////////****PRIVATE****/////////////////////

    /**
     * closeClient beended die Verbindung zuwischen Server und Client.
     * @param session Das Sessionobjekt des zu schließenden Clienten.
     */
    private void closeClient( final Session session) {
        try {
            sendMsg("Verbindung wird getrennt!",session);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getGameID gibt ein Spielobject zu einer bestimmten GameID wieder.
     * @param gameID Die ID des Spiel.
     * @return Das Spielobjekt mit der gesuchten ID. Falls das Spielobjekt nicht existiert, wird das errorGame-Objekt zurückgegeben.
     */
    private Game getGameByID(Integer gameID) {
        if(gameSessions.containsKey(gameID))
            return gameSessions.get(gameID);
        else {
            System.out.println("ERROR: A GameSession with Key:" + gameID + " does not exist!");
            return errorGame;
        }
    }

    /**
     * parseGameID Einen String in GameID(int) umwandeln.
     * @param str_gameID Der zu konvertierende String.
     * @return die konvertierte GameID als int
     */
    private Integer parseGameID(String str_gameID) {
        int gameID = 0;

        try {
            gameID = Integer.parseInt(str_gameID);
        }
        catch(NumberFormatException e)
        {
            gameID = -1;
        }

        return gameID;
    }

    /**
     * deleteGameByID löscht ein bestimmtes Spielobjekt aus der Hashmap.
     * @param gameID Die ID des Spielobjektes welches gelöscht werden soll.
     */
    private void deleteGameByID(Integer gameID) {
        if(gameSessions.containsKey(gameID))
        {
            Game game = gameSessions.get(gameID);

            if(game.closeGame())
            {
                if(gameSessions.remove(gameID,game)) {
                    System.out.println("Game with gameID:" + gameID + " was successfully deleted!");
                }
            }
            else
            {
                System.out.println("ERROR: While trying to close Game with gameID:" + gameID);
            }
        }
        else{
            System.out.println("ERROR: The game with gameID:" + gameID + " does not exist and can't be deleted!");
        }
    }
}