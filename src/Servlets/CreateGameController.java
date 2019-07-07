package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.CreateGameBean;

@WebServlet("/createGame")
public class CreateGameController extends HttpServlet
{
    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CreateGameBean newGameBean = new CreateGameBean();

        if (request.getParameterMap().containsKey("gameType")) {
            String gameType = request.getParameter("gameType");
            newGameBean.setType(gameType);

            if (newGameBean.getSuccess()) {
                String newGameID = newGameBean.getGameID();
                response.sendRedirect("/" + gameType + "/" + newGameID);
            }
        }
        else
        {
            request.getServletContext().getRequestDispatcher("/").forward(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }
}
