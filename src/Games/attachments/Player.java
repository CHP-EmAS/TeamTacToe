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

    public Player(Session playerSession) {
        socketSession = playerSession;

        if(playerSession == null)
        {
            httpSessionID = "NULL_SESSION";
            return;
        }

        HttpSession temp = (HttpSession) socketSession.getUserProperties().get("sessionID");

        httpSessionID = temp.getId();
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
