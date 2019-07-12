package Servlets;

import Interfaces.DatabaseConnection;
import org.json.JSONObject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getBestPlayers")
public class ajaxLeaderboard extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameterMap().containsKey("limit"))
            response.getWriter().write(DatabaseConnection.getBestPlayers(Integer.parseInt(request.getParameter("limit"))).toString());
        else
            response.getWriter().write(DatabaseConnection.getBestPlayers(10).toString());
    }
}
