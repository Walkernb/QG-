package service.tiezi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.tiezi.tieziServlet;
import dao.CommentsDao;
import dao.TiebaDao;
import dao.TieziDao;
import entity.UserEntity;
import entity.tiebaEntity;
import entity.tieziEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TieziActionServlet extends tieziServlet {

    /**
     * 创建帖子
     * @param request
     * @param response
     */
    public void build(HttpServletRequest request, HttpServletResponse response)  {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        tiebaEntity tieba = (tiebaEntity) request.getSession().getAttribute("tieba");

        tieziEntity tz = new tieziEntity();
        tz.setName(request.getParameter("tzname"));
        tz.setTexts(request.getParameter("text"));
        tz.setuId(user.getId());
        tz.setTbId(tieba.getId());

        boolean b = new TieziDao().createTiezi(tz);
        if(b){
            try {
                response.sendRedirect("/QGtieba2/tiezi/showTiezi.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示某个贴吧里帖子的信息
     * @param request
     * @param response
     */
    public void select(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        tiebaEntity tieba = (tiebaEntity) request.getSession().getAttribute("tieba");    //获取某个贴吧的信息
        List<tieziEntity> list = new TieziDao().getByTbId(tieba.getId());

        if(list!=null){                                               //存在相关的帖子，则输出
            ObjectMapper mapper=new ObjectMapper();
            try {
                String str = mapper.writeValueAsString(list);
                response.getWriter().write(str);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{                                                        //若不存在相关的帖子，则输出null
            try {
                response.getWriter().write("null");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除帖子
     * @param request
     * @param response
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");                         //将被删除帖子的id
        String selector = request.getParameter("selector");             //跳转的方向
        new CommentsDao().delete(id);
        new TieziDao().delete(id);
        if("1".equals(selector)){
            response.sendRedirect("/QGtieba2/tieba/manageTiezi.html");  //跳转到我的贴吧下的帖子
        }else   if("2".equals(selector)){
            response.sendRedirect("/QGtieba2/tiezi/myTiezi.html");      //跳转到我的帖子
        }
    }

    /**
     * 将帖子的信息保存到session，并跳转到展示帖子的具体内容的页面，或管理帖子的页面
     * @param request
     * @param response
     */
    public void setSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String id = request.getParameter("id");
        String selector = request.getParameter("selector");                 //跳转方向
        tieziEntity tz = new TieziDao().getByid(id);
        request.getSession().setAttribute("tiezi",tz);
        if("1".equals(selector)){
            response.sendRedirect("/QGtieba2/tiezi/tieziDetail.html");          //跳转到显示帖子具体内容的页面
        }else   if("2".equals(selector)){
            response.sendRedirect("/QGtieba2/comments/manageComments.html");    //跳转到管理帖子的页面
        }
    }

    /**
     * 发送(存在session中)帖子的信息
     * @param request
     * @param response
     */
    public void send(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        tieziEntity tz = (tieziEntity) request.getSession().getAttribute("tiezi");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writeValueAsString(tz);
            response.getWriter().write(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新点赞数
     * @param request
     * @param response
     */
    public void update(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        tieziEntity tz = (tieziEntity) request.getSession().getAttribute("tiezi");
        int likes = tz.getLikes();
        tz.setLikes(likes+1);
        new TieziDao().update(tz);
        request.getSession().setAttribute("tiezi",tz);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writeValueAsString(tz);
            response.getWriter().write(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示用户的帖子
     * @param request
     * @param response
     */
    public void myTiezi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        List<tieziEntity> list = new TieziDao().getByUid(user.getId());
        if(list!=null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                String data = mapper.writeValueAsString(list);
                response.getWriter().write(data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else {
            response.getWriter().write("null");
        }
    }

    /**
     * 搜索帖子
     * @param request
     * @param response
     */
    public void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String search = request.getParameter("search");
        String text = "%"+search+"%";
        tiebaEntity tb = (tiebaEntity) request.getSession().getAttribute("tieba");
        List<tieziEntity> list = new TieziDao().search(text, tb.getId());
        if(list!=null){
            request.getSession().setAttribute("tzList",list);
            request.getSession().setAttribute("searchResultTz","success");
            response.sendRedirect("/QGtieba2/tiezi/searchResult.html");
        }else{
            request.getSession().setAttribute("searchResultTz","failure");
            response.sendRedirect("/QGtieba2/tiezi/showTiezi.html");
        }
    }

    /**
     * 检查搜索结果
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
        String searchResultTz = (String) request.getSession().getAttribute("searchResultTz");
        response.getWriter().write(searchResultTz);
    }

    /**
     * 显示查询结果
     * @param request
     * @param response
     * @throws IOException
     */
    public void searchResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        List<tieziEntity> tzList = (List<tieziEntity>) request.getSession().getAttribute("tzList");
        String data = new ObjectMapper().writeValueAsString(tzList);
        response.getWriter().write(data);
    }

    /**
     * 帖子排序
     * @param request
     * @param response
     * @throws IOException
     */
    public void sort(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        tiebaEntity tieba = (tiebaEntity) request.getSession().getAttribute("tieba");
        String type = request.getParameter("type");
        List<tieziEntity> list = null;
        if("time".equals(type)){
            list = new TieziDao().getByTbId(tieba.getId());
        }else if("comments".equals(type)){
            list = new TieziDao().sortByComments(tieba.getId());
        }else if("likes".equals(type)){
            list = new TieziDao().sortByLikes(tieba.getId());
        }

        if(list!=null){
            String data = new ObjectMapper().writeValueAsString(list);
            response.getWriter().write(data);
        }else{
            response.getWriter().write("null");
        }
    }
}
