package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import beans.UserBean;

@WebServlet("/Register")
public class RegisterController extends HttpServlet
{
    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //Wenn ein angemeldeter Nutzer in der Session existiert -> löschen
        if(request.getSession(false) != null)
        {
            if(request.getSession().getAttribute("user") != null)
                request.getSession().removeAttribute("user");
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
                userBean.setAlert("Passwoerter stimmen nicht ueberein");
                request.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
            }

            userBean.setPassword(password);

            userBean.register();

            if(userBean.isValid())
            {
                request.getSession(true).setAttribute("user", userBean);
                response.sendRedirect("/");
            }
            else request.getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
        }
        else response.sendRedirect("/register.jsp");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }
}
