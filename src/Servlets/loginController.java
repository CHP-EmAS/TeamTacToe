package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import javaBeans.UserBean;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class loginController extends HttpServlet
{
    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Wenn ein angemeldeter Nutzer in der Session existiert, auf home zurückkehren
        if(request.getSession(false) != null)
        {
            if(request.getSession().getAttribute("user") != null) {
                if(((UserBean) request.getSession().getAttribute("user")).getLoggedIn())
                {
                    ((UserBean) request.getSession().getAttribute("user")).setAlert("Du bist bereits angemeldet");
                    response.sendRedirect("/");
                    return;
                }
            }
        }

        UserBean userBean = new UserBean();
        request.getSession().setAttribute("user", userBean);

        Map<String,String[]> paraMap = request.getParameterMap();

        if (paraMap.containsKey("nickname") && paraMap.containsKey("pw"))
        {
            String nickname = request.getParameter("nickname");
            String passwd = request.getParameter("pw");

            userBean.setNickname(nickname);
            userBean.setPassword(passwd);

            userBean.validateLogin();

            if(userBean.getLoggedIn())
            {
                response.sendRedirect("/");
            }
            else response.sendRedirect("/");
        }
        else response.sendRedirect("/");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }
}
