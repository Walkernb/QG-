package controller.user;

import service.user.UserActionServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String methodName = request.getParameter("method");
        UserActionServlet uaServlet = new UserActionServlet();
        if("login".equals(methodName)){
            uaServlet.login(request,response);
        }else if("add".equals(methodName)){
            uaServlet.add(request,response);
        }else if("send".equals(methodName)){
            uaServlet.send(request, response);
        }else if("alter".equals(methodName)){
            uaServlet.alter(request, response);
        }else if("alterPSW".equals(methodName)){
            uaServlet.alterPassword(request, response);
        }else if("find".equals(methodName)){
            uaServlet.findbackPassword(request, response);
        }else if("alterPhoto".equals(methodName)){                  //修改头像
            uaServlet.alterPhoto(request, response);
        }else if("check".equals(methodName)){
            uaServlet.check(request, response);                //验证用户的输入
        }
    }
}
