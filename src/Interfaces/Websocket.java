package Interfaces;

import Games.*;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint(value = "/play" , configurator = HttpSessionOverride.class) //@ServerEndpoint: Der Server-Websocket hört auf die URL-Endung /play und nimmt nur darüber neue Verbindungen auf.

public class Websocket {

    /////////////////////****VARIABLES****/////////////////////
    private static final int GAME_ID_LENGTH = 11;


    //Statische Hashmap wird zum Speichern der Spielobjekte und dessen jeweilige GameID verwendet
    private static HashMap<String, Game> gameSessions = new HashMap<>();


    //Ein übergabe Objekt für fehlerhafte Spielobjekte
    private static Game errorGame;

    /////////////////////****PUBLIC****/////////////////////
    /**
     * Funktion OnOpen wird aufgerufen wenn sich ein neuer Websocketclient verbindet.
     * Sie verteilt die neuen Verbindungen auf die jeweiligen Spielobjekte und erstellt ggf. neue Spielobjekte.
     * @param session: die Session die sich mit dem Server verbunden hat.
     */
    @OnOpen
    public void open(final Session session, EndpointConfig config) throws IOException{

        HttpSession httpSession = (HttpSession) config.getUserProperties().get("sessionID");
        System.out.println("New User connected (SessionID:" + httpSession.getId() + ")");

        session.setMaxIdleTimeout(300000);
    }

    /**
     * Funktion OnMessage nimmt Nachrichten der Clients entgegen und verteilt diese auf die jeweilgen Spielobjekte.
     * @param message: Die jeweilige Nachricht als String.
     * @param session: Das Sessionobjekt des sendeden Clients.
     */
    @OnMessage
    public void onMessage(String message, final Session session) {
        String httpSessionID = ((HttpSession)session.getUserProperties().get("sessionID")).getId();

        JSONObject obj = new JSONObject(message);

        if(obj.has("forward"))
        {
            String gameID = obj.getString("forward");

            if(gameSessions.containsKey(gameID))
            {
                System.out.println("Message from User " + httpSessionID + " forwarded to Game " + gameID + ": " + message);
                getGameByID(gameID).receiveMessage(message, httpSessionID);
            }
        }
        else
        {
            System.out.println("Message from User " + httpSessionID + ": " + message);

            if(obj.has("cmd"))
            {
                switch (obj.getString("cmd")) {
                    case "createNewGame":

                        String gameType = obj.getString("type");
                        Game.GameType type = Game.getGameType(gameType);

                        if(type != Game.GameType.NONE)
                        {
                            String newGameID = createGame(type);

                            if(newGameID.length() == GAME_ID_LENGTH)
                            {
                                JSONObject json = new JSONObject();

                                json.put("cmd","game_created");
                                json.put("gameType",type.shortcut());
                                json.put("gameID",newGameID);

                                sendMsg(json.toString(),session);
                            }
                        }

                        break;
                    case "login":

                        break;
                    case "connect":
                        if(gameSessions.containsKey(obj.getString("gameID"))) getGameByID(obj.getString("gameID")).addPlayer(session,obj.getString("nickname"),obj.getString("passwd"));
                        break;
                }
            }
        }
    }

    /**
     * Funktion OnOpen wird aufgerufen wenn eine Verbindung zu einem Client geschlossen oder abgebrochen wurde.
     * @param session Das Sessionobjekt des schließenden Clients.
     */
    @OnClose
    public void onClose(final Session session){
        String httpSessionID = ((HttpSession)session.getUserProperties().get("sessionID")).getId();

        String gameID = "";
        if(session.getRequestParameterMap().containsKey("gameID")) gameID = session.getRequestParameterMap().get("gameID").get(0);

        if(gameSessions.containsKey(gameID))
        {
            if(getGameByID(gameID).removePlayer(httpSessionID)) {
                System.out.println("User " + httpSessionID + " in Game:" + gameID + " lost connection and was removed from Game!");
                if (getGameByID(gameID).getPlayerAmount() <= 0) {
                    deleteGameByID(gameID);
                }
            }
            else {
                System.out.println("User " + httpSessionID + " in Game:" + gameID + " lost connection");
                System.out.println("ERROR: User " + httpSessionID + " doesn't exist in Game:" + gameID + "!");
            }
        }
        else
        {
            System.out.println("User " + httpSessionID + " lost connection!");
        }
    }

    /**
     * sendMsg sended eine Nachricht an einen bestimmten Client.
     * @param msg Nachricht als String.
     * @param session Das Sessionobjekt des Empfängers.
     * @return Boolean, zeigt an ob die Nachricht erfolgreich gesendet wurde.
     */
    private Boolean sendMsg(String msg, final Session session) {
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

    private String createGame(Game.GameType gameType) {
        String newGameID = "";
        int count = GAME_ID_LENGTH;

        do {
            StringBuilder builder = new StringBuilder();

            while (count-- != 0) {

                final String alpha_string_numeric = "abcdefghijklmnopqrstuwxyz0123456789";
                int character = (int) (Math.random() * alpha_string_numeric.length());

                builder.append(alpha_string_numeric.charAt(character));
            }

            newGameID = builder.toString();
        }
        while(gameSessions.containsKey(newGameID));

        Game newGame;

        switch(gameType) {
            case TicTacToe:
                newGame  = new TicTacToe(newGameID);
                break;
            case Super_TicTacToe:
            case Fancy_TicTacToe:
            case Inception_TicTacToe:
            default:
                System.out.println("Websocket: ERROR: Spieltype <" + gameType.toString() + "> ist unbekannt!");
                return "";
        }

        gameSessions.put(newGameID,newGame);

        return newGameID;
    }

    /**
     * getGameID gibt ein Spielobject zu einer bestimmten GameID wieder.
     * @param gameID Die ID des Spiel.
     * @return Das Spielobjekt mit der gesuchten ID. Falls das Spielobjekt nicht existiert, wird das errorGame-Objekt zurückgegeben.
     */
    private Game getGameByID(String gameID) {
        if(gameSessions.containsKey(gameID))
            return gameSessions.get(gameID);
        else {
            System.out.println("ERROR: A GameSession with Key:" + gameID + " does not exist!");
            return errorGame;
        }
    }

    /**
     * deleteGameByID löscht ein bestimmtes Spielobjekt aus der Hashmap.
     * @param gameID Die ID des Spielobjektes welches gelöscht werden soll.
     */
    private void deleteGameByID(String gameID) {
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