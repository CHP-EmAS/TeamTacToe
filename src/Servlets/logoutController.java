package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class logoutController extends HttpServlet
{
    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Wenn ein angemeldeter Nutzer in der Session existiert -> löschen
        if(request.getSession(false) != null) {
            if (request.getSession().getAttribute("user") != null) {
                request.getSession().removeAttribute("user");
            }

            response.sendRedirect("/");
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }
}
