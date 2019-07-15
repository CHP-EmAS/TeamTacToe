package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import javaBeans.UserBean;

@WebServlet(name = "register", urlPatterns = {"/register"})
public class registerController extends HttpServlet
{
    private static final int MAX_NICKNAME_LENGHT = 30;
    private static final int MIN_NICKNAME_LENGHT = 3;

    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //Wenn ein angemeldeter Nutzer in der Session existiert -> löschen
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
        request.setAttribute("userBean", userBean);

        Map<String,String[]> paraMap = request.getParameterMap();

        if (paraMap.containsKey("nickname") && paraMap.containsKey("pw") && paraMap.containsKey("repeatPw"))
        {
            String nickname = request.getParameter("nickname");
            String password = request.getParameter("pw");
            String repeatPw = request.getParameter("repeatPw");

            userBean.setNickname(nickname);

            if(!password.equals(repeatPw))
            {
                userBean.setAlert("Die Passwörter sind nicht identisch!");
                request.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            else if(nickname.length() < MIN_NICKNAME_LENGHT )
            {
                userBean.setAlert("Benutzername muss mindestens " + MIN_NICKNAME_LENGHT + " Zeichen lang sein!");
                request.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            else if(nickname.length() > MAX_NICKNAME_LENGHT)
            {
                userBean.setAlert("Benutzername darf höchstens " + MAX_NICKNAME_LENGHT + " Zeichen lang sein!");
                request.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            userBean.register(password);

            if(userBean.getLoggedIn())
            {
                request.getSession(true).setAttribute("user", userBean);
                response.sendRedirect("/");
            }
            else request.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
        }
        else request.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }
}
