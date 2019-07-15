package Games.attachments;

import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import javaBeans.UserBean;

public class Player {
    private Session socketSession;
    private String httpSessionID;

    private UserBean user;

    public Player(Session playerSession) {
        socketSession = playerSession;
        if(playerSession == null)
        {
            httpSessionID = "NULL_SESSION";
            user = null;

            return;
        }

        HttpSession tempSession = (HttpSession) socketSession.getUserProperties().get("session");

        httpSessionID = tempSession.getId();

        user = (UserBean) tempSession.getAttribute("user");

        System.out.println("Player: Player <" + getPlayerName() + "> added.");
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

    public void sendInfoMessage(String msg) {
        JSONObject json = new JSONObject();
        json.put("cmd","infoMsg");
        json.put("content",msg);

        sendMessage(json.toString());
    }

    public String getPlayerName() {
        if(user != null) {
            if (user.getLoggedIn())
                return user.getNickname();
        }
        else if(httpSessionID.equals("NULL_SESSION"))
            return "Error Player";

        return "Gast";
    }

    public Boolean isRegisteredPlayer() {
        if(user != null) return user.getLoggedIn();
        else return false;
    }

    public void addPoints(int amount)
    {
        if(user != null) user.addPoints(amount);
    }

    public String getHttpSessionID(){return httpSessionID;}
    public Session getSession(){return socketSession;}
}
