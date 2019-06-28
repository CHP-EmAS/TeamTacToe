package Games.attachments;

import Interfaces.DatabaseConnection;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;

public class Player {
    private Session socketSession;
    private String httpSessionID;

    private String nickname;
    private Boolean registeredPlayer;

    public Player(Session playerSession, Boolean registeredPlayer,String nickname, String hash_playerPassword) {
        socketSession = playerSession;
        HttpSession temp = (HttpSession) socketSession.getUserProperties().get("sessionID");
        httpSessionID = temp.getId();

        if(registeredPlayer)
        {
            DatabaseConnection conn = new DatabaseConnection("TeamTacToe","tomcat","tomcat");
            if(conn.isReady())
            {
                conn.executeQuery("SELECT nickname WHERE nickname='" + nickname + "' AND passwd='" + hash_playerPassword + "';");
            }
            else System.out.println("Datenbankverbindung fehlgeschlagen!");
        }
    }

    public Boolean sendMessageToPlayer(String msg) {
        try {
            socketSession.getBasicRemote().sendText(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
