package Servlets;

import Games.Game;
import Interfaces.Websocket;
import org.json.JSONObject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/gameController")
public class gameController extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getParameterMap().containsKey("mode"))
        {
            switch(request.getParameter("mode"))
            {
                case "createGame":

                    if (request.getParameterMap().containsKey("gameType"))
                    {
                        Game.GameType type = Game.getGameType(request.getParameter("gameType"));
                        String generatedGameID = Websocket.createGame(type);

                        if(!generatedGameID.equals("")) response.getWriter().write("/" + type.shortcut() + "/" + generatedGameID);
                        else response.getWriter().write("/");
                    }
                    else response.getWriter().write("/");

                    break;
                case "gameExists":
                    JSONObject obj = new JSONObject();

                    String value;
                    String gameType = Game.GameType.NONE.shortcut();

                    if (request.getParameterMap().containsKey("gameID"))
                    {
                        value = Websocket.gameExists(request.getParameter("gameID")).toString();

                        if(value.equals("true")) gameType = Websocket.getGameType(request.getParameter("gameID")).shortcut();
                    }
                    else value = "false";

                    obj.put("req",value);
                    obj.put("type",gameType);

                    response.getWriter().write(obj.toString());

                    break;
                default:
                    response.getWriter().write("Error");
                    break;
            }

        }
    }
}
