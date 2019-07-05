package Beans;

import Interfaces.Websocket;
import Games.Game;

public class CreateGameBean
{
    private String generatedGameID;
    private Game.GameType type;
    private boolean success;

    public CreateGameBean()
    {
        generatedGameID = "";
        success = false;
    }

    public void setType(String type)
    {
        this.type = Game.getGameType(type);

        generatedGameID = Websocket.createGame(this.type);
        success = generatedGameID.equals("");
    }

    public String getGameID()
    {
        return generatedGameID;
    }

    public boolean getSuccess()
    {
        return success;
    }
}
