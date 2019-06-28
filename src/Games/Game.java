package Games;

import javax.websocket.Session;
import java.io.IOException;

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
    protected Gamestate gamestate;
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
     * @param session Sessionobjekt des Clients.
     * @return Boolen gibt an ob die Operation erfolgreich war.
     */
    public Boolean removePlayer(final Session session)
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
     * @param session Sessionobjekt des Clients.
     * @return Boolen gibt an ob sich der Spieler im Spiel befindet.
     */
    public Boolean isPlayerInGame(final Session session)
    {
        return false;
    }

    /**
     * receiveMessage wird aufgerufen wenn eine Nachricht durch den Websock an dies Spielobjekt empfangen wurde.
     * @param cmd String - ist der Inhalt der Nachricht.
     * @param player Sessionobjekt des Senders.
     */
    public void receiveMessage(String cmd, final Session player) {}

    /**
     * closeGame schließt ein Spielobjekt sicher. Alle Verbindungen werden dabei getrennt.
     * @return Boolen gibt an ob die Operation erfolgreich war.
     */
    public Boolean closeGame()
    {
        return false;
    }
}

enum Gamestate
{
    WAITING_FOR_PLAYER,
    RUNNING,
    PAUSED,
    RESTARTING
}
