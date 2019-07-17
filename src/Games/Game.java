package Games;

import javax.websocket.Session;
import Games.attachments.*;
import Interfaces.Websocket;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Game Klasse - Parent-Objekt zu den jeweiligen Spieltypen
 *
 * !!!!!!!!!!!!
 * Für neue erbende Spieltypen müssen die Funktionen
 * - addPlayer
 * - removePlayer
 * - getPlayerAmount
 * - isPlayerInGame
 * - receiveMessage
 * - closeGame
 * überschrieben werden.
 * !!!!!!!!!!!!
 */
public class Game
{
    public enum GameType {
        TicTacToe("TTT"),
        Super_TicTacToe("STTT"),
        Fancy_TicTacToe("FTTT"),
        Inception_TicTacToe("ITTT"),
        ERROR("ERROR"),
        NONE("NONE");

        private final String shortcut;
        GameType(String shortcut) { this.shortcut = shortcut; }
        public String shortcut() { return this.shortcut; }
    }

    public enum Gamestate {
        WAITING_FOR_PLAYER,
        RUNNING,
        PAUSED,
        CLOSED,
        CREATED
    }

    protected String gameID;
    protected final static Player errorPlayer = new Player(null);
    protected Gamestate gamestate;
    protected final GameType gametype;

    private Timer t_checkPlayer = new Timer();
    private TimerTask checkTask = new TimerTask() {
        int ticks = 0;

        @Override
        public void run() {
            ticks++;

            if(getPlayerAmount() == 0 || ticks >= 240)
            {
                System.out.println("GameTimer: Game <"+ gameID +", " + gametype + "> will be deleted, because the number of players in game is 0 or game exists since 2 hours!");
                Websocket.deleteGameByID(gameID);

                t_checkPlayer.cancel();
                t_checkPlayer.purge();
            }
        }
    };

    public Game(GameType type) {
        gameID = "";
        gametype = type;
        gamestate = Gamestate.CREATED;

        if(gametype != GameType.ERROR && gametype != GameType.NONE) t_checkPlayer.scheduleAtFixedRate(checkTask,5*1000,30*1000);
    }

    /**
     * addPlayer fügt Sessionobjekte(Spieler) zum Spiel hinzu.
     * @param session Sessionobjekt des Clients.
     * @return Boolen gibt an ob die Operation erfolgreich war.
     */
    public Boolean addPlayer(final Session session)
    {
        return false;
    }

    /**
     * removePlayer löscht  Sessionobjekte(Spieler) aus dem Spiel.
     * @param httpSessionID Sessionobjekt des Clients.
     * @return Boolen gibt an ob die Operation erfolgreich war.
     */
    public Boolean removePlayer(String httpSessionID)
    {
        return false;
    }

    /**
     * getPlayerAmount gibt die Anzahl der Spielen im Spiel zurück.
     * @return Integer - Anzahl der Spieler.
     */
    public Integer getPlayerAmount()
    {
        return 0;
    }

    /**
     * isPlayerInGame zeig an ob ein bestimmter Spieler im Spiel ist.
     * @param httpSessionID Sessionobjekt des Clients.
     * @return Boolen gibt an ob sich der Spieler im Spiel befindet.
     */
    public Boolean isPlayerInGame(String httpSessionID)
    {
        return false;
    }

    /**
     * receiveMessage wird aufgerufen wenn eine Nachricht durch den Websock an dies Spielobjekt empfangen wurde.
     * @param cmd String - ist der Inhalt der Nachricht.
     * @param httpSessionID Sessionobjekt des Senders.
     */
    public void receiveMessage(String cmd, String httpSessionID) {}

    /**
     * closeGame schließt ein Spielobjekt sicher. Alle Verbindungen werden dabei getrennt.
     * @return Boolen gibt an ob die Operation erfolgreich war.
     */
    public Boolean closeGame()
    {
        t_checkPlayer.cancel();
        t_checkPlayer.purge();

        return true;
    }

    public Player getPlayer(String httpSessionID) { return errorPlayer; }

    public GameType getGameType() { return this.gametype; }

    public static GameType getGameType(String shortcut) {
        switch (shortcut) {
            case "TTT":
                return GameType.TicTacToe;
            case "STTT":
                return GameType.Super_TicTacToe;
            case "FTTT":
                return GameType.Fancy_TicTacToe;
            case "ITTT":
                return GameType.Inception_TicTacToe;
            case "ERROR":
                return GameType.ERROR;
            default:
                return GameType.NONE;
        }
    }
}
