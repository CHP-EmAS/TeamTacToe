package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import Beans.LoginRegisterBean;

@WebServlet("/login")
public class loginController extends HttpServlet
{
    private void doGetOrPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginRegisterBean LRBean = new LoginRegisterBean();
        request.setAttribute("loginBean", LRBean);

        Map<String,String[]> paraMap = request.getParameterMap();

        if (paraMap.containsKey("nickname") && paraMap.containsKey("passwd") && paraMap.containsKey("method"))
        {
            String nickname = request.getParameter("nickname");
            String passwd = request.getParameter("passwd");
            String method = request.getParameter("method");

            LRBean.setMethod(method);
            LRBean.setNickname(nickname);
            LRBean.setPassword(passwd);

            if(LRBean.getSuccess()) request.getSession().setAttribute("nickname", nickname);

            request.setAttribute("alert",LRBean.getAlert());
            request.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        }
        else
        {
            request.setAttribute("alert","Upps :( , da ist etwas schief gelaufen!");
            request.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGetOrPost(request, response);
    }
}
