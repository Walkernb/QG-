package service.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.comment.commentServlet;
import dao.CommentsDao;
import dao.TieziDao;
import entity.UserEntity;
import entity.commentEntity;
import entity.tieziEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class commentActionServlet extends commentServlet {
    /**
     * 通过帖子的id，返回相应评论的数据
     * @param request
     * @param response
     */
    public void getBytzId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        tieziEntity tz = (tieziEntity) request.getSession().getAttribute("tiezi");
        List<commentEntity> list = new CommentsDao().getBytzId(tz.getId());
        if(list!=null){
            ObjectMapper map = new ObjectMapper();
            try {
                String data = map.writeValueAsString(list);
                response.getWriter().write(data);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            response.getWriter().write("null");                 //无评论
        }
    }

    /**
     * 创建评论
     * @param request
     * @param response
     * @throws IOException
     */
    public void build(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        tieziEntity tz = (tieziEntity) request.getSession().getAttribute("tiezi");
        commentEntity entity = new commentEntity();
        entity.setTexts(request.getParameter("comment"));
        entity.setuId(user.getId());
        entity.setTzId(tz.getId());
        entity.setPhoto("null");
        boolean b = new CommentsDao().build(entity);
        if(b){                                                  //创建成功
            int n = tz.getNumber();                             //评论数
            tz.setNumber(n+1);                                  //评论数加一
            new TieziDao().update(tz);
            request.getSession().setAttribute("tiezi",tz);
            response.sendRedirect("/QGtieba2/tiezi/tieziDetail.html");
        }
    }

    /**
     * 通过id，删除对应的评论
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
        String id = request.getParameter("id");
        tieziEntity tz = (tieziEntity) request.getSession().getAttribute("tiezi");
        boolean b = new CommentsDao().delById(id);
        if(b){
            tz.setNumber(tz.getNumber()-1);
            new TieziDao().update(tz);                              //更新帖子的评论数
            request.getSession().setAttribute("tiezi",tz);
            response.sendRedirect("/QGtieba2/comments/manageComments.html");
        }
    }
}
