package controller.tieba;

import service.tieba.TiebaActionServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "tiebaServlet", value = "/tiebaServlet")
public class tiebaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String methodName = request.getParameter("method");
        TiebaActionServlet tbAservlet = new TiebaActionServlet();
        if("build".equals(methodName)){
            tbAservlet.build(request,response);
        }else   if("data".equals(methodName)){
            tbAservlet.data(request, response);
        }else if("select".equals(methodName)){
            tbAservlet.select(request, response);
        }else if("send".equals(methodName)){
            tbAservlet.send(request, response);
        }else if("myTieba".equals(methodName)){
            tbAservlet.myTieba(request, response);
        }else if("alter".equals(methodName)){
            tbAservlet.alter(request, response);
        }else if("search".equals(methodName)){
            tbAservlet.search(request, response);
        }else if("checkResult".equals(methodName)){
            tbAservlet.checkResult(request, response);
        }else if("searchResult".equals(methodName)){
            tbAservlet.searchResult(request, response);
        }else if("getByStatus".equals(methodName)){
            tbAservlet.getByStatus(request, response);
        }else if("agree".equals(methodName)){
            tbAservlet.agreeBuild(request, response);
        }
    }
}
