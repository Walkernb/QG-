package controller.tiezi;

import service.tiezi.TieziActionServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "tieziServlet", value = "/tieziServlet")
public class tieziServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String method = request.getParameter("method");
        TieziActionServlet tzAserlvet = new TieziActionServlet();

        if("build".equals(method)){
            tzAserlvet.build(request, response);
        }else if("select".equals(method)){
            tzAserlvet.select(request, response);
        }else if("delete".equals(method)){
            tzAserlvet.delete(request, response);
        }else if("reserve".equals(method)){
            tzAserlvet.setSession(request, response);
        }else if("send".equals(method)){
            tzAserlvet.send(request, response);
        }else if("update".equals(method)){
            tzAserlvet.update(request, response);
        }else if("getByUid".equals(method)){
            tzAserlvet.myTiezi(request, response);
        }else if("search".equals(method)){
            tzAserlvet.search(request, response);
        }else if("checkResult".equals(method)){
            tzAserlvet.checkResult(request, response);
        }else if("searchResult".equals(method)){
            tzAserlvet.searchResult(request, response);
        }else if("sort".equals(method)){
            tzAserlvet.sort(request, response);
        }
    }
}
