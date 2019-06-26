package Games.attachments;

import Interfaces.DatabaseConnection;
import javax.websocket.Session;
import java.io.IOException;

public class Player {
    private Session socketSession;
    private String httpSessionID;

    private String nickname;
    private Boolean registeredPlayer;

    public Player(String nickname, Boolean registeredPlayer) {
        if(registeredPlayer)
        {
            DatabaseConnection conn = new DatabaseConnection("xxxULTIMATE_DATABASExxx");
            if(conn.isReady()) System.out.println("Datenbankverbindung erfolgreich!");
            else System.out.println("Datenbankverbindung fehlgeschlagen!");
        }
    }

    public Boolean sendMessage(String msg, Session socketSession) {
        try {
            socketSession.getBasicRemote().sendText(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
