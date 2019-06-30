package Games.attachments;

import Interfaces.DatabaseConnection;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.sql.*;

public class Player {
    private Session socketSession;
    private String httpSessionID;

    private String nickname;
    private Boolean registeredPlayer;

    public Player(Session playerSession, Boolean registeredPlayer,String nickname, String passwd) {
        this.nickname = nickname;
        socketSession = playerSession;

        if(playerSession == null)
        {
            httpSessionID = "NULL_SESSION";
            registeredPlayer = false;
            return;
        }

        HttpSession temp = (HttpSession) socketSession.getUserProperties().get("sessionID");
        httpSessionID = temp.getId();

        if(registeredPlayer)
        {
            DatabaseConnection conn = new DatabaseConnection("TeamTacToe","tomcat","tomcat");
            if(conn.isReady())
            {
                ResultSet rs = conn.executeQuery("SELECT COUNT(*) AS amount FROM user WHERE nickname='" + nickname + "' AND passwd=PASSWORD('" + passwd + "');");

                try {
                    if (rs.first()) {
                        this.registeredPlayer = (rs.getInt("amount") == 1);
                    }
                }
                catch(SQLException e) { e.printStackTrace(); }
            }
            else System.out.println("Datenbankverbindung fehlgeschlagen!");
        }
        else this.registeredPlayer = false;
    }

    public Boolean sendMessage(String msg) {
        try {
            socketSession.getBasicRemote().sendText(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean sendInfoMessage(String msg) {
        JSONObject json = new JSONObject();
        json.put("cmd","infoMsg");
        json.put("content",msg);

        return sendMessage(json.toString());
    }

    public String getPlayerName(){return nickname;}
    public String getHttpSessionID(){return httpSessionID;}
    public Boolean isRegisteredPlayer(){return registeredPlayer;}
    public Session getSession(){return socketSession;}
}
