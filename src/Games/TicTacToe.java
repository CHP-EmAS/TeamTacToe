package Games;

import Games.attachments.*;

import org.json.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class TicTacToe extends Game
{
    private LittleField fieldData;

    private Player playerOne,playerTwo;
    private Player currentPlayer = null;

    public TicTacToe(String gameID) {
        super(GameType.TicTacToe);

        super.gameID = gameID;
        fieldData = new LittleField();

        playerOne = null;
        playerTwo = null;

        gamestate = Gamestate.WAITING_FOR_PLAYER;
    }

    @Override
    public Boolean addPlayer(final Session playerSession)
    {
        String httpSessionID = ((HttpSession)playerSession.getUserProperties().get("session")).getId();

        if(gamestate == Gamestate.RUNNING || isPlayerInGame(httpSessionID)) return false;

        if(playerOne == null){
            playerOne = new Player(playerSession);
            if(playerTwo == null) playerOne.sendInfoMessage("Warte auf Mitspieler!");
        }
        else if(playerTwo == null)
        {
            playerTwo = new Player(playerSession);
            if(playerOne == null) playerTwo.sendInfoMessage("Warte auf Mitspieler!");
        }
        else return false;

        if(getPlayerAmount() == 2)
        {
            if(currentPlayer == null)
            {
                Random generator = new Random();
                if(generator.nextInt(2) == 1) setCurrentPlayer(playerOne);
                else setCurrentPlayer(playerTwo);
            }

            updateUserField();
            gamestate = Gamestate.RUNNING;
        }

        return true;
    }

    @Override
    public Boolean removePlayer(String httpSessionID)
    {
        if(playerOne.getHttpSessionID().equals(httpSessionID))
        {
            if(playerOne == currentPlayer) currentPlayer = null;
            playerOne = null;

            if(playerTwo != null)  playerTwo.sendInfoMessage("Mitspieler hat das Spiel verlassen!");

            return true;
        }
        else if(playerTwo.getHttpSessionID().equals(httpSessionID))
        {
            if(playerTwo == currentPlayer) currentPlayer = null;
            playerTwo = null;

            if(playerOne != null) playerOne.sendInfoMessage("Mitspieler hat das Spiel verlassen!");

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
    public Boolean isPlayerInGame(String httpSessionID)
    {
        if(playerOne != null) {
            if (httpSessionID.equals(playerOne.getHttpSessionID()))
                return true;
        }

        if(playerTwo != null) {
            if(httpSessionID.equals(playerTwo.getHttpSessionID()))
                return true;
        }

        return false;
    }

    @Override
    public void receiveMessage(String cmd, String httpSessionID) {
        if(!isPlayerInGame(httpSessionID)) return;

        Player sender;

        if(playerOne.getHttpSessionID().equals(httpSessionID)) sender = playerOne;
        else sender = playerTwo;

        JSONObject obj = new JSONObject(cmd);

        if(obj.has("cmd"))
        {
            switch(obj.getString("cmd"))
            {
                case "click":
                    fieldClick(obj.getInt("fieldNum"), sender);
                    break;
                case "reset":
                    if(getPlayerAmount() == 2) {
                        if(currentPlayer != null) setCurrentPlayer(currentPlayer);
                        else setCurrentPlayer(playerOne);
                        fieldData.reset();
                        updateUserField();
                        gamestate = Gamestate.RUNNING;
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
                playerOne.sendInfoMessage("Spiel wird beendet!");
                playerOne.getSession().close();
                playerOne = null;
            }

            if(playerTwo != null)
            {
                playerTwo.sendInfoMessage("Spiel wird beendet!");
                playerTwo.getSession().close();
                playerTwo = null;
            }

            gameID = "";
            gamestate = Gamestate.CLOSED;

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Player getPlayer(String httpSessionID)
    {
        if(playerOne.getHttpSessionID().equals(httpSessionID)) return playerOne;
        else if(playerTwo.getHttpSessionID().equals(httpSessionID)) return playerTwo;
        else return errorPlayer;
    }

    private void fieldClick(int fieldNum, Player player)
    {
        if(gamestate == Gamestate.RUNNING)
        {
            if (playerOne != null && playerTwo != null) {
                if (fieldNum >= 0 && fieldNum < 9) {
                    if (player == currentPlayer) {
                        if (fieldData.getTile(fieldNum).getPlayer() == 0) {

                            if (player.equals(playerOne)) fieldData.getTile(fieldNum).setPlayer(1);
                            else if (player.equals(playerTwo)) fieldData.getTile(fieldNum).setPlayer(2);

                            updateUserField();

                            int gameResult = fieldData.getResult();

                            switch (gameResult) {
                                case -1:
                                    playerOne.sendInfoMessage("Unentschieden!");
                                    playerTwo.sendInfoMessage("Unentschieden!");
                                    playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
                                    gamestate = Gamestate.PAUSED;
                                    return;
                                case 0:
                                    if(player.equals(playerOne)) setCurrentPlayer(playerTwo);
                                    else setCurrentPlayer(playerOne);
                                    return;
                                case 1:
                                    playerOne.sendInfoMessage("Du hast gewonnen!");
                                    playerTwo.sendInfoMessage("Du hast verloren!");
                                    playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
                                    gamestate = Gamestate.PAUSED;
                                    return;
                                case 2:
                                    playerTwo.sendInfoMessage("Du hast gewonnen!");
                                    playerOne.sendInfoMessage("Du hast verloren!");
                                    playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
                                    gamestate = Gamestate.PAUSED;
                                    return;
                            }
                        } else {
                            player.sendInfoMessage("Ungültiger Zug!");
                        }
                    } else {
                        player.sendInfoMessage("Dein Mitspieler ist am Zug!");
                    }
                } else {
                    System.out.println("Fehler beim Klicken eines Feldes! Field <" + fieldNum + "> out of range");
                }
            } else {
                player.sendInfoMessage("Ungültiger Zug! Sie brauchen einen Mitspieler!");
            }
        }
    }

    /**
     * Setzen, welcher Spieler am Zug ist. Player One und PLayer Two müssen initialisiert sein!
     * Muss initialisiert werden vor Spielbeginn!
     * @param player
     * @return True bei Erfolg. False bei Misserfolg
     */
    private void setCurrentPlayer(Player player) {
        if(playerOne != null && playerTwo != null) {
            if (player.equals(playerOne)) {
                currentPlayer = playerOne;
                playerOne.sendInfoMessage("Du bist dran!");
                playerTwo.sendInfoMessage("Mitspieler wählt ein Feld!");
                return;
            }

            if (player.equals(playerTwo)) {
                currentPlayer = playerTwo;
                playerTwo.sendInfoMessage("Du bist dran!");
                playerOne.sendInfoMessage("Mitspieler wählt ein Feld!");
                return;
            }

            System.out.println("ERROR: Ungültige Spielersession! Spieler=" + player.getHttpSessionID() + " Game=" + gameID);
            player.sendInfoMessage("ERROR: Ungültige Spielersession!");
        }
        else System.out.println("ERROR: Spieler noch nicht initialisiert!");
    }

    private void updateUserField()
    {
        String msg = "{\"cmd\":\"field\",\"fieldData\":" + Arrays.toString(fieldData.getFieldArray()) + "}";

        if(playerOne != null) playerOne.sendMessage(msg);
        if(playerTwo != null) playerTwo.sendMessage(msg);
    }
}
