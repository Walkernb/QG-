package controller.follow;

import service.follow.followActionServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "followServlet", value = "/followServlet")
public class followServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String method = request.getParameter("method");
        followActionServlet service = new followActionServlet();
        if("followTb".equals(method)){
            service.followTb(request, response);
        }else if("followTz".equals(method)){
            service.followTz(request, response);
        }else if("showTb".equals(method)){
            service.showTb(request, response);
        }else if("deleteTb".equals(method)){
            service.deleteTb(request, response);
        }else if("showTz".equals(method)){
            service.showTz(request, response);
        }else if("deleteTz".equals(method)){
            service.deleteTz(request, response);
        }
    }
}
