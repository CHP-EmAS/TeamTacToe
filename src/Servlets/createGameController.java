package Servlets;

import Games.Game;
import Interfaces.Websocket;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/createGame")
public class createGameController extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameterMap().containsKey("gameType"))
        {
            Game.GameType type = Game.getGameType(request.getParameter("gameType"));
            String generatedGameID = Websocket.createGame(type);

            if(!generatedGameID.equals(""))
            {
                response.getWriter().write("/" + type.shortcut() + "/" + generatedGameID);
            }
            else
            {
                response.getWriter().write("/");
            }
        }
        else
        {
            response.getWriter().write("/");
        }
    }
}
