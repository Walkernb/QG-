package service.tieba;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.tieba.tiebaServlet;
import dao.TiebaDao;
import entity.UserEntity;
import entity.tiebaEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TiebaActionServlet extends tiebaServlet {
    /**
     * 创建贴吧
     * @param request
     * @param response
     */
    public void build(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        tiebaEntity tb = new tiebaEntity();
        String tbName = request.getParameter("tbname");
        String notice = request.getParameter("notice");
        tb.setName(tbName);
        tb.setNotice(notice);
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");       //获取用户的信息
        tb.setuId(user.getId());

        boolean b = new TiebaDao().createTieba(tb);
        if(b==true){
            try {
                response.sendRedirect("/QGtieba2/interFace/interface.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

        }
    }

    /**
     * 显示所有贴吧的信息
     * @param request
     * @param response
     */
    public void data(HttpServletRequest request, HttpServletResponse response){
        List<tiebaEntity> tb = new TiebaDao().tiebaData();                  //获取所有贴吧的信息
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str = null;             //将集合转化为JSON格式
        try {
            str = mapper.writeValueAsString(tb);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //System.out.println(str);
        writer.write(str);
    }

    /**
     * 储存指定的贴吧信息，并跳转
     * @param request
     * @param response
     */
    public void select(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String selector = request.getParameter("selector");                 //跳转方向
        String id = request.getParameter("id");
        tiebaEntity tb = new TiebaDao().getById(Integer.parseInt(id));          //获取指定id的贴吧

        request.getSession().setAttribute("tieba",tb);                   //储存贴吧

        if("1".equals(selector)){
            response.sendRedirect("/QGtieba2/tiezi/showTiezi.html");        //跳转到显示帖子的页面
        }else if("2".equals(selector)){
            response.sendRedirect("/QGtieba2/tieba/manageTiezi.html");      //跳转到某个贴吧下管理帖子的页面
        }
    }

    /**
     * 发送一个 (储存在session里的) 贴吧的信息 （显示某个贴吧内具体内容）
     * @param request
     * @param response
     */
    public void send(HttpServletRequest request, HttpServletResponse response)  {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tiebaEntity tieba = (tiebaEntity) request.getSession().getAttribute("tieba");

        if(tieba!=null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                response.setContentType("text/html;charset=utf-8");
                String str = mapper.writeValueAsString(tieba);
                response.getWriter().write(str);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回某个用户创建的贴吧(个人贴吧)
     * @param request
     * @param response
     */
    public void myTieba(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        List<tiebaEntity> list = new TiebaDao().getByUid(user.getId());
        if(list==null){
            response.getWriter().write("null");
        }else{
            ObjectMapper map = new ObjectMapper();
            try {
                String str = map.writeValueAsString(list);
                response.getWriter().write(str);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改贴吧的公告
     * @param request
     * @param response
     * @throws IOException
     */
    public void alter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");

        String notice = request.getParameter("notice");
        tiebaEntity tieba = (tiebaEntity) request.getSession().getAttribute("tieba");       //贴吧数据
        boolean b = new TiebaDao().alter(tieba, notice);

        if(b){
            tieba.setNotice(notice);
            request.getSession().setAttribute("tieba",tieba);
            response.sendRedirect("/QGtieba2/tieba/manageTiezi.html");
        }else{

        }

    }

    /**
     * 搜索贴吧
     * @param request
     * @param response
     * @throws IOException
     */
    public void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String search = request.getParameter("search");
        String text = "%"+search+"%";                       //处理查询内容
        List<tiebaEntity> list = new TiebaDao().search(text);
        if(list!=null){
            request.getSession().setAttribute("tbList",list);
            request.getSession().setAttribute("searchResultTb","success");
            response.sendRedirect("/QGtieba2/tieba/searchResult.html");
        }else{
            request.getSession().setAttribute("searchResultTb","failure");
            response.sendRedirect("/QGtieba2/interFace/interface.html");
        }
    }

    /**
     * 检查查询结果
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String searchResult = (String) request.getSession().getAttribute("searchResultTb");
        response.getWriter().write(searchResult);
    }

    /**
     * 显示查询结果
     * @param request
     * @param response
     */
    public void searchResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        List<tiebaEntity> tbList = (List<tiebaEntity>) request.getSession().getAttribute("tbList");
        String data = new ObjectMapper().writeValueAsString(tbList);
        response.getWriter().write(data);
    }

    /**
     * 显示待审核的贴吧
     * @param request
     * @param response
     */
    public void getByStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if("Y".equals(user.getStatus())){
            List<tiebaEntity> list = new TiebaDao().getByStatus();
            if(list!=null){
                String data = new ObjectMapper().writeValueAsString(list);
                response.getWriter().write(data);
            }else{
                response.getWriter().write("null");
            }
        }else{
            response.getWriter().write("N");
        }
    }

    /**
     * 管理员审核通过，可以创建贴吧
     * @param request
     * @param response
     */
    public void agreeBuild(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        new TiebaDao().agreeBuild(id);
        response.sendRedirect("/QGtieba2/user/managerAuthority.html");
    }
}
