package service.follow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.follow.followServlet;
import dao.FollowDao;
import dao.TiebaDao;
import entity.*;
import service.tieba.tiebaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class followActionServlet extends followServlet {
    /**
     * 添加用户关注的贴吧
     * @param request
     * @param response
     */
    public void followTb(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        tiebaEntity tb = (tiebaEntity) request.getSession().getAttribute("tieba");
        followTb fTb = new followTb();
        fTb.setuId(user.getId());
        fTb.setTbId(tb.getId());
        boolean b = new FollowDao().addtb(fTb);
        if(b){
            String data = new ObjectMapper().writeValueAsString("success");
            response.getWriter().write(data);
        }else{
            String data = new ObjectMapper().writeValueAsString("failure");
            response.getWriter().write(data);
        }
    }

    /**
     * 添加用户收藏的帖子
     * @param request
     * @param response
     */
    public void followTz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        tieziEntity tz = (tieziEntity) request.getSession().getAttribute("tiezi");
        followTz ftz = new followTz();
        ftz.setuId(user.getId());
        ftz.setTzId(tz.getId());
        boolean b = new FollowDao().addtz(ftz);
        PrintWriter writer = response.getWriter();
        if(b){
            String str = new ObjectMapper().writeValueAsString("success");
            writer.write(str);
        }else {
            String str = new ObjectMapper().writeValueAsString("failure");
            writer.write(str);
        }
    }

    /**
     * 显示关注的贴吧
     * @param request
     * @param response
     */
    public void showTb(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        List<tiebaEntity> list = new tiebaService().getById(user.getId());
        if(list!=null){
            String data = new ObjectMapper().writeValueAsString(list);
            response.getWriter().write(data);
        }else{
            response.getWriter().write("null");
        }
    }

    /**
     * 删除数据库里，用户关注贴吧的数据
     * @param request
     * @param response
     * @throws IOException
     */
    public void deleteTb(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        String id = request.getParameter("id");
        new FollowDao().deleteTb(id,user.getId());
        response.sendRedirect("/QGtieba2/tieba/followTb.html");
    }

    /**
     * 显示用户关注的帖子
     * @param request
     * @param response
     * @throws IOException
     */
    public void showTz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        List<tieziEntity> list = new FollowDao().getByUid_Tz(user.getId());
        if(list!=null){
            String data = new ObjectMapper().writeValueAsString(list);
            response.getWriter().write(data);
        }else{
            response.getWriter().write("null");
        }
    }

    /**
     * 删除用户收藏的帖子
     * @param request
     * @param response
     * @throws IOException
     */
    public void deleteTz(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        String id = request.getParameter("id");
        new FollowDao().deleteTz(id, user.getId());
        response.sendRedirect("/QGtieba2/tiezi/followTz.html");
    }
}
