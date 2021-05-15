package dao;

import entity.UserEntity;
import util.JDBCutils;
import util.Password;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Userdao {

    /**
     * 注册用户
     * @param rUser 用户输入的数据
     * @return flag 执行成功，返回true，不成功，返回false
     */
    public boolean Register(UserEntity rUser){
        boolean flag=true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql="insert into user values(null,?,?,?,'N','qq.jpeg')";       //sql语句的设置与执行
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,rUser.getUsername());
            String psw = Password.md5(rUser.getPassword());                       //加密
            pstmt.setString(2,psw);
            pstmt.setString(3,rUser.getPhone());
            int i = pstmt.executeUpdate();
            if(i!=1){                                                   //判断执行情况
                flag=false;
                cnn.rollback();
            }else{
                cnn.commit();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if(cnn!=null){
                try {
                    cnn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }

        return flag;
    }

    /**
     * 登录
     * @param lUser
     * @return
     */
    public UserEntity Login(UserEntity lUser){
        UserEntity user = new UserEntity();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="select * from user where username = ? and password = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,lUser.getUsername());
            String psw = Password.md5(lUser.getPassword());                 //加密
            pstmt.setString(2,psw);
            rs = pstmt.executeQuery();
            boolean b = rs.next();
            if(b!=true){
                user = null;
            }else{
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                String status = rs.getString("status");
                String photo = rs.getString("photo");
                user.setId(id);
                user.setUsername(username);
                user.setPassword(password);
                user.setPhone(phone);
                user.setStatus(status);
                user.setPhoto(photo);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return user;
    }

    /**
     * 修改用户名和电话号码
     * @param user
     * @return
     */
    public boolean Alter(UserEntity user){
        boolean flag=true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql="UPDATE USER SET username=?, phone=? WHERE id=?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPhone());
            pstmt.setInt(3,user.getId());
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else{
                cnn.rollback();
                flag=false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                if(cnn!=null){
                    cnn.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            flag=false;
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
        return flag;
    }

    /**
     * 修改头像
     * @param user
     * @param photo
     * @return
     */
    public boolean Alter(UserEntity user,String photo){
        boolean flag=true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "UPDATE USER SET photo = ? WHERE id = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,photo);
            pstmt.setInt(2,user.getId());
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else{
                cnn.rollback();
                flag=false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if(cnn!=null){
                try {
                    cnn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            flag=false;
        }
        return flag;
    }

    /**
     * 修改密码
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public boolean Alter(UserEntity user,String oldPassword,String newPassword){
        boolean flag=true;
        if(user.getPassword().equals(Password.md5(oldPassword))){                       //验证旧密码是否正确
            Connection cnn = null;
            PreparedStatement pstmt = null;
            try {
                cnn = JDBCutils.getConnection();
                cnn.setAutoCommit(false);
                String sql="UPDATE USER SET PASSWORD=? WHERE id=?;";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setString(1,Password.md5(newPassword));
                pstmt.setInt(2,user.getId());
                int i = pstmt.executeUpdate();
                if(i==1){
                    cnn.commit();
                }else{
                    cnn.rollback();
                    flag=false;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                try {
                    if(cnn!=null){
                        cnn.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                flag=false;
            }
        }else{
            flag=false;
        }
        return flag;
    }

    /**
     * 返回新注册用户的id
     * @param user
     * @return
     */
    public int getId(UserEntity user){
        int id=0;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="select id from user where username = ? ;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,user.getUsername());
            rs = pstmt.executeQuery();
            rs.next();
            id = rs.getInt("id");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return id;
    }

    /**
     * 返回用户信息
     * @param username
     * @param phone
     * @return
     */
    public UserEntity getUser(String username, String phone){
        UserEntity user = new UserEntity();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="SELECT * FROM USER WHERE username= ? AND phone= ? ;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,phone);
            rs = pstmt.executeQuery();
            if(rs.next()){
                user.setId(rs.getInt("id"));
                user.setUsername(username);
                user.setPassword(rs.getString("password"));
                user.setPhone(phone);
                user.setStatus(rs.getString("status"));
                user.setPhoto(rs.getString("photo"));
            }else{
                user=null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return user;
    }

    /**
     * 找回密码，储存新密码
     * @param user
     */
    public void findback(UserEntity user){
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "UPDATE USER SET PASSWORD= ? WHERE id = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,user.getPassword());
            pstmt.setInt(2,user.getId());
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else{
                cnn.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
    }

}
