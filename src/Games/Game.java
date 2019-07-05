package Games;

import javax.websocket.Session;
import Games.attachments.*;
import java.util.HashMap;

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

    public Game(GameType type) {
        gameID = "";
        gametype = type;
        gamestate = Gamestate.CREATED;
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
        return false;
    }

    public Player getPlayer(String httpSessionID) { return errorPlayer; }

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
            default:
                return GameType.NONE;
        }
    }
}
