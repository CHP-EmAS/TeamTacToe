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
    private static final int GAME_ID_LENGTH = 11; //Länge der zu generierenden GameID. Darf nicht 0 sein
    private static final int MAX_GAME_AMOUNT = 100; //Maximale Spiel anzahl

    private static HashMap<String, Game> gameSessions = new HashMap<>(); //Hashmap wird zum Speichern der Spielobjekte und dessen jeweilige GameID verwendet

    private static Game errorGame = new Game(Game.GameType.NONE); //Rückgabewert für ein nicht gefundenes Game

    /////////////////////****PUBLIC****/////////////////////
    /**
     * Funktion OnOpen wird aufgerufen wenn sich ein neuer Websocketclient verbindet.
     * Sie verteilt die neuen Verbindungen auf die jeweiligen Spielobjekte und erstellt ggf. neue Spielobjekte.
     * @param session: die Session die sich mit dem Server verbunden hat.
     */
    @OnOpen
    static public void open(final Session session, EndpointConfig config){

        HttpSession httpSession = (HttpSession) config.getUserProperties().get("sessionID");
        System.out.println("Websocket: New Socket opened with SessionID: <" + httpSession.getId() + ">");

        session.setMaxIdleTimeout(300000);
    }

    /**
     * Funktion OnMessage nimmt Nachrichten der Clients entgegen und verteilt diese auf die jeweilgen Spielobjekte.
     * @param message: Die jeweilige Nachricht als String.
     * @param session: Das Sessionobjekt des sendeden Clients.
     */
    @OnMessage
    static public void onMessage(String message, final Session session) {
        String httpSessionID = ((HttpSession)session.getUserProperties().get("sessionID")).getId();

        JSONObject obj = new JSONObject(message);

        if(obj.has("forward"))
        {
            String gameID = obj.getString("forward");

            if(gameSessions.containsKey(gameID))
            {
                System.out.println("Websocket: Message from Socket <" + httpSessionID + "> forwarded to Game <" + gameID + ">: " + message);

                if(getGameByID(gameID).isPlayerInGame(httpSessionID)) getGameByID(gameID).receiveMessage(message, httpSessionID);
                else closeClient(session,"Cannot forward Message to Game <" + obj.getString("forward") + ">. Game does not contains SessionID!");
            }
            else closeClient(session,"Cannot forward Message to Game <" + obj.getString("forward") + ">. Game does not exist!");

        }
        else
        {
            System.out.println("Websocket: Message from Socket <" + httpSessionID + ">: " + message);

            if(obj.has("cmd"))
            {
                switch (obj.getString("cmd")) {
                    case "createNewGame":
                        if(obj.has("type")){
                            String gameType = obj.getString("type");
                            Game.GameType type = Game.getGameType(gameType);

                            if (type != Game.GameType.NONE){
                                String newGameID = createGame(type);

                                if(!newGameID.equals("")){
                                    JSONObject json = new JSONObject();

                                    json.put("cmd", "game_created");
                                    json.put("gameType", type.shortcut());
                                    json.put("gameID", newGameID);

                                    sendMsg(json.toString(), session);
                                }
                                else closeClient(session,"Cannot create Game!");
                            }
                        }
                        else closeClient(session,"Missing Variables in Command <createNewGame>");

                        break;
                    case "login":

                        break;
                    case "connect":
                        if(obj.has("gameID") && obj.has("nickname") && obj.has("passwd")) {
                            if(gameSessions.containsKey(obj.getString("gameID"))) {
                                if(!getGameByID(obj.getString("gameID")).isPlayerInGame(httpSessionID)) {
                                    if(!getGameByID(obj.getString("gameID")).addPlayer(session))
                                        closeClient(session, "Unable to add to Game <" + obj.getString("gameID") + ">");
                                }
                                else closeClient(session, "Unable to add to Game <" + obj.getString("gameID") + "> Instance of Session <" + httpSessionID + "> alredy exists in Game <" + obj.getString("gameID") + ">!");
                            }
                            else closeClient(session, "Game <" + obj.getString("gameID") + "> not found!");
                        }
                        else closeClient(session,"Missing Variables in Command <connect>");

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
    static public void onClose(final Session session){
        String httpSessionID = ((HttpSession)session.getUserProperties().get("sessionID")).getId();

        String gameID = "";
        if(session.getRequestParameterMap().containsKey("gameID")) gameID = session.getRequestParameterMap().get("gameID").get(0);

        if(gameSessions.containsKey(gameID)) {
            if(getGameByID(gameID).isPlayerInGame(httpSessionID)) {
                if(getGameByID(gameID).getPlayer(httpSessionID).getSession().getId().equals(session.getId())) {
                    if(getGameByID(gameID).removePlayer(httpSessionID)) {
                        System.out.println("Websocket: Socket <" + httpSessionID + "> in Game <" + gameID + "> lost connection and was removed from Game!");
                        if (getGameByID(gameID).getPlayerAmount() <= 0) {
                            deleteGameByID(gameID);
                        }
                    } else System.out.println("Websocket: Socket <" + httpSessionID + "> in Game <" + gameID + "> lost connection");
                }else System.out.println("Websocket: Socket <" + httpSessionID + "> lost connection! Same HttpSessionID as Player in Game <" + gameID + ">, but SocketID doesn't match!");
            }else System.out.println("Websocket: ERROR: Socket <" + httpSessionID + "> lost connection, but doesn't exist in Game <" + gameID + ">!");
        }else System.out.println("Websocket: Socket <" + httpSessionID + "> lost connection!");
    }

    /**
     * sendMsg sended eine Nachricht an einen bestimmten Client.
     * @param msg Nachricht als String.
     * @param session Das Sessionobjekt des Empfängers.
     */
    static  private void sendMsg(String msg, final Session session) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /////////////////////****PRIVATE****/////////////////////

    /**
     * closeClient beended die Verbindung zuwischen Server und Client.
     * @param session Das Sessionobjekt des zu schließenden Clienten.
     */
    static private void closeClient(final Session session) {
        try {
            sendMsg("Verbindung wird getrennt!",session);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private void closeClient(final Session session, String closeReason) {
        sendMsg(closeReason,session);
        closeClient(session);
    }

    static public String createGame(Game.GameType gameType) {
        if(gameSessions.size() >= MAX_GAME_AMOUNT) return "";

        String newGameID;

        //Zeichen die GamID enthalten darf
        final String alpha_string_numeric = "ABCDEFGHIJKLMNOPQRSTUVW0123456789";

        //GameID generieren
        do {
            StringBuilder builder = new StringBuilder();
            int count = GAME_ID_LENGTH;

            while (count-- != 0) {
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
                newGame  = new SuperTicTacToe(newGameID);
                break;
            case Fancy_TicTacToe:
            case Inception_TicTacToe:
            default:
                System.out.println("Websocket: ERROR: Unknown Gametype <" + gameType.toString() + ">!");
                return "";
        }

        gameSessions.put(newGameID,newGame);

        System.out.println("Websocket: Game <" + newGameID + "> was sucessfully created!");
        return newGameID;
    }

    /**
     * getGameID gibt ein Spielobject zu einer bestimmten GameID wieder.
     * @param gameID Die ID des Spiel.
     * @return Das Spielobjekt mit der gesuchten ID. Falls das Spielobjekt nicht existiert, wird das errorGame-Objekt zurückgegeben.
     */
    static private Game getGameByID(String gameID) {
        if(gameSessions.containsKey(gameID))
            return gameSessions.get(gameID);
        else {
            System.out.println("Websocket: ERROR: A GameSession with Key:" + gameID + " does not exist!");
            return errorGame;
        }
    }

    /**
     * deleteGameByID löscht ein bestimmtes Spielobjekt aus der Hashmap.
     * @param gameID Die ID des Spielobjektes welches gelöscht werden soll.
     */
    static private void deleteGameByID(String gameID) {
        if(gameSessions.containsKey(gameID))
        {
            Game game = gameSessions.get(gameID);

            if(game.closeGame())
            {
                if(gameSessions.remove(gameID,game)) System.out.println("Websocket: Game with gameID <" + gameID + "> was successfully deleted!");
            }
            else System.out.println("Websocket: ERROR: While trying to close Game with gameID <" + gameID + ">");
        }
        else System.out.println("Websocket: ERROR: The game with gameID <" + gameID + "> does not exist and can't be deleted!");
    }
}