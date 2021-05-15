package service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.user.UserServlet;
import dao.Userdao;
import entity.UserEntity;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import util.FileupoadUtils;
import util.Password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

public class UserActionServlet extends UserServlet {
    /**
     * 用户登录
     * @param request
     * @param response
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String code = request.getParameter("checkCode");    //验证码
        String checkCode = (String) request.getSession().getAttribute("checkCode");
        if(checkCode.equalsIgnoreCase(code)){
            UserEntity lUser = new UserEntity();
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            lUser.setUsername(username);
            lUser.setPassword(password);

            UserEntity user = new Userdao().Login(lUser);

            response.setContentType("text/html;charset=utf-8");
            if(user!=null){
                HttpSession session = request.getSession();
                session.setAttribute("user",user);                      //储存登录用户的信息
                session.setAttribute("check","success");
                response.sendRedirect("/QGtieba2/interFace/interface.html");
            }else{
                request.getSession().setAttribute("check","failure");
                response.sendRedirect("/QGtieba2/user/login.html");     //跳转回登录页面
            }
        }else{                                                             //验证码错误
            request.getSession().setAttribute("check","checkCode");
            response.sendRedirect("/QGtieba2/user/login.html");     //跳转回登录页面
        }
    }

    /**
     * 验证登录，修改密码，找回密码的输入是否正确
     * @param request
     * @param response
     */
    public void check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String check = (String) request.getSession().getAttribute("check");
        if(check.equals("failure")){
            response.getWriter().write("failure");
        }
    }

    /**
     * 注册
     * @param request
     * @param response
     */
    public void add(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UserEntity rUser = new UserEntity();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        rUser.setUsername(username);
        rUser.setPassword(password);
        rUser.setPhone(phone);

        boolean b = new Userdao().Register(rUser);
        response.setContentType("text/html;charset=utf-8");
        if (b) {
            int id = new Userdao().getId(rUser);
            rUser.setId(id);
            rUser.setStatus("N");
            System.out.println(rUser);
            HttpSession session = request.getSession();
            session.setAttribute("user", rUser);                        //储存注册用户的信息
            try {
                response.sendRedirect("/QGtieba2/interFace/interface.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }


    /**
     * 发送用户信息
     * @param request
     * @param response
     */
    public void send(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if(user!=null){
            response.setContentType("text/html;charset=utf-8");
            try {
                ObjectMapper mapper=new ObjectMapper();
                String str = mapper.writeValueAsString(user);
                response.getWriter().write(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改用户名和电话号码
     * @param request
     * @param response
     */
    public void alter(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String username = request.getParameter("username");
        String phone = request.getParameter("phone");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");       //获取原来的用户信息
        user.setUsername(username);
        user.setPhone(phone);

        boolean b = new Userdao().Alter(user);
        if(b){
            request.getSession().setAttribute("user",user);
            try {
                response.sendRedirect("/QGtieba2/user/information.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改密码
     * @param request
     * @param response
     */
    public void alterPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");

        boolean b = new Userdao().Alter(user, oldPassword, newPassword);
        if(b){
            user.setPassword(Password.md5(newPassword));
            request.getSession().setAttribute("user",user);
            request.getSession().setAttribute("check","success");
            response.sendRedirect("/QGtieba2/user/information.html");
        }else{
            request.getSession().setAttribute("check","failure");
            response.sendRedirect("/QGtieba2/user/alterPassword.html");
        }
    }

    /**
     * 修改头像
     * @param request
     * @param response
     */
    public void alterPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        String fileName = FileupoadUtils.alterPhoto(request, response);                 //文件上传
        boolean b = new Userdao().Alter(user, fileName);
        System.out.println("文件名"+fileName);
        if(b){
            user.setPhoto(fileName);
            request.getSession().setAttribute("user",user);
            response.sendRedirect("/QGtieba2/user/information.html");
        }
    }

    /**
     * 找回密码
     * @param request
     * @param response
     */
    public void findbackPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String newPassword = request.getParameter("newPassword");
        UserEntity user = new Userdao().getUser(name, phone);               //判断是否存在符合用户提供信息的用户
        if(user!=null){                                                      //存在
            user.setPassword(Password.md5(newPassword));
            new Userdao().findback(user);
            request.getSession().setAttribute("user",user);
            request.getSession().setAttribute("check","success");
            response.sendRedirect("/QGtieba2/interFace/interface.html");
        }else{
            request.getSession().setAttribute("check","failure");
            response.sendRedirect("/QGtieba2/user/findback.html");
        }
    }

}
