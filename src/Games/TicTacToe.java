package Games;

import Games.attachments.*;

import org.json.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Arrays;

public class TicTacToe extends Game
{
    private int gameID;
    private LittleField fieldData;

    private Player playerOne,playerTwo;
    private Player currentPlayer = null;

    public TicTacToe(int gameID) {
        this.gameID = gameID;
        fieldData = new LittleField();

        playerOne = null;
        playerTwo = null;

        gamestate = Gamestate.WAITING_FOR_PLAYER;
    }

    @Override
    public Boolean addPlayer(final Session playerSession)
    {
        String httpSessionID = ((HttpSession)playerSession.getUserProperties().get("sessionID")).getId();

        if(httpSessionID.equals(playerOne.getHttpSessionID()) || httpSessionID.equals(playerTwo.getHttpSessionID())) return true;

        if(playerOne == null){
            playerOne = new Player(playerSession,true,"EmAS","*47A6B0EA08A36FAEBE4305B373FE37E3CF27C357")
            playerOne = player;
            if(playerTwo == null) sendInfoMessage("Warte auf Mitspieler!",playerOne);
        }
        else if(playerTwo == null)
        {
            playerTwo = player;
        }
        else return false;

        if(getPlayerAmount() == 2)
        {
            if(currentPlayer == null) setCurrentPlayer(player);
            updateField();
            gameIsRunning = true;
        }

        return true;
    }

    @Override
    public Boolean removePlayer(final Session session)
    {
        if(playerOne == session)
        {
            if(playerOne == currentPlayer) currentPlayer = null;
            playerOne = null;

            if(playerTwo != null)  sendInfoMessage("Mitspieler hat das Spiel verlassen!",playerTwo);

            return true;
        }
        else if(playerTwo == session)
        {
            if(playerTwo == currentPlayer) currentPlayer = null;
            playerTwo = null;

            if(playerOne != null) sendInfoMessage("Mitspieler hat das Spiel verlassen!",playerOne);

            return true;
        }

        return false;
    }

    @Override
    public Integer getPlayerAmount()
    {
        int pAmount = 0;

        if(playerOne != null) pAmount++;
        if(playerTwo != null) pAmount++;

        return pAmount;
    }

    @Override
    public Boolean isPlayerInGame(final Session session)
    {
        if(playerOne != null) {
            if (session.getId().equals(playerOne.getId()))
                return true;
        }

        if(playerTwo != null) {
            if(session.getId().equals(playerTwo.getId()))
                return true;
        }

        return false;
    }

    @Override
    public void receiveMessage(String cmd, final Session player) {
        JSONObject obj = new JSONObject(cmd);

        if(obj.has("cmd"))
        {
            switch(obj.getString("cmd"))
            {
                case "click":
                    fieldClick(obj.getInt("fieldNum"), player);
                    break;
                case "reset":
                    if(getPlayerAmount() == 2) {
                        if(currentPlayer != null) setCurrentPlayer(currentPlayer);
                        else setCurrentPlayer(playerOne);
                        fieldData.reset();
                        updateField();
                        gameIsRunning = true;
                    }
                    break;
            }
        }
    }

    @Override
    public Boolean closeGame()
    {
        try {
            if(playerOne != null)
            {
                sendMsg("Spiel wird beendet!",playerOne);
                playerOne.close();
                playerOne = null;
            }

            if(playerTwo != null)
            {
                sendMsg("Spiel wird beendet!",playerTwo);
                playerTwo.close();
                playerTwo = null;
            }

            gameID = -1;
            gameIsRunning = false;

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void fieldClick(int fieldNum, final Session player)
    {
        if(gameIsRunning)
        {
            if (playerOne != null && playerTwo != null) {
                if (fieldNum > 0 && fieldNum < 10) {
                    if (player == currentPlayer) {
                        if (fieldData.getTile(fieldNum).getPlayer() == 0) {

                            if (player == playerOne) fieldData.getTile(fieldNum).setPlayer(1);
                            else if (player == playerTwo) fieldData.getTile(fieldNum).setPlayer(2);

                            updateField();

                            int gameResult = fieldData.getResult();

                            switch (gameResult) {
                                case -1:
                                    sendInfoMessage("Unentschieden!", playerOne);
                                    sendInfoMessage("Unentschieden!", playerTwo);
                                    sendMsg("{\"cmd\":\"enableReset\"}",playerOne);
                                    gameIsRunning = false;
                                    return;
                                case 0:
                                    if(player == playerOne) setCurrentPlayer(playerTwo);
                                    else setCurrentPlayer(playerOne);
                                    return;
                                case 1:
                                    sendInfoMessage("Du hast gewonnen!", playerOne);
                                    sendInfoMessage("Du hast verloren!", playerTwo);
                                    sendMsg("{\"cmd\":\"enableReset\"}",playerOne);
                                    gameIsRunning = false;
                                    return;
                                case 2:
                                    sendInfoMessage("Du hast gewonnen!", playerTwo);
                                    sendInfoMessage("Du hast verloren!", playerOne);
                                    sendMsg("{\"cmd\":\"enableReset\"}",playerOne);
                                    gameIsRunning = false;
                                    return;
                            }
                        } else {
                            sendInfoMessage("Ungültiger Zug!", player);
                        }
                    } else {
                        sendInfoMessage("Dein Mitspieler ist am Zug!", player);
                    }
                } else {
                    System.out.println("Fehler beim Klicken eines Feldes! Field <" + fieldNum + "> out of range");
                }
            } else {
                sendInfoMessage("Ungültiger Zug! Sie brauchen einen Mitspieler!", player);
            }
        }
    }

    /**
     * Setzen, welcher Spieler am Zug ist. Player One und PLayer Two müssen initialisiert sein!
     * Muss initialisiert werden vor Spielbeginn!
     * @param player
     * @return True bei Erfolg. False bei Misserfolg
     */
    public boolean setCurrentPlayer(Session player) {
        if(playerOne != null && playerTwo != null) {
            if (player.equals(playerOne)) {
                this.currentPlayer = this.playerOne;
                sendInfoMessage("Du bist dran!", playerOne);
                sendInfoMessage("Mitspieler wählt ein Feld!", playerTwo);
                return true;
            }

            if (player.equals(playerTwo)) {
                this.currentPlayer = this.playerTwo;
                sendInfoMessage("Du bist dran!", playerTwo);
                sendInfoMessage("Mitspieler wählt ein Feld!", playerOne);
                return true;
            }

            System.out.println("ERROR: Ungültige Spielersession! Spieler=" + player.getId() + " Game=" + gameID);
            sendInfoMessage("ERROR: Ungültige Spielersession!", player);
        }
        else
            System.out.println("ERROR: Spieler noch nicht initialisiert!");

        return false;
    }

    private void sendInfoMessage(String msg, final Session player)
    {
        String msg_json = "{\"cmd\":\"msg\",\"content\":\"" + msg + "\"}";
        sendMsg(msg_json,player);
    }

    private void updateField()
    {
        String msg = "{\"cmd\":\"field\",\"fieldData\":" + Arrays.toString(fieldData.getFieldArray()) + "}";

        if(playerOne != null) sendMsg(msg,playerOne);
        if(playerTwo != null) sendMsg(msg,playerTwo);
    }
}
