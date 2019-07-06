package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import Beans.UserBean;

@WebServlet("/Validate")
public class loginController extends HttpServlet
{
    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserBean userBean = new UserBean();
        request.setAttribute("userBean", userBean);

        Map<String,String[]> paraMap = request.getParameterMap();

        if (paraMap.containsKey("nickname") && paraMap.containsKey("passwd"))
        {
            String nickname = request.getParameter("nickname");
            String passwd = request.getParameter("passwd");

            userBean.setNickname(nickname);
            userBean.setPassword(passwd);

            userBean.validateLogin();

            if(userBean.isValid())
            {
                request.getSession(true).setAttribute("user", userBean);
                response.sendRedirect("index.jsp");
            }
            else request.getServletContext().getRequestDispatcher("/Login").forward(request, response);
        }
        else
        {
            response.sendRedirect("Login");
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }
}
