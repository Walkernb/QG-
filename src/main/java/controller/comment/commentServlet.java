package controller.comment;

import service.comment.commentActionServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "commentServlet", value = "/commentServlet")
public class commentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        commentActionServlet cmServlet = new commentActionServlet();
        String method = request.getParameter("method");
        if("getBytzId".equals(method)){
            cmServlet.getBytzId(request, response);
        }else if("build".equals(method)){
            cmServlet.build(request, response);
        }else if("delete".equals(method)){
            cmServlet.delete(request, response);
        }
    }
}
