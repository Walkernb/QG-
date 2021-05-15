package dao;

import entity.commentEntity;
import jdk.nashorn.internal.scripts.JD;
import util.JDBCutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsDao {


    /**
     * 删除某帖子里所有评论
     * @param tzId
     */
    public void delete(String tzId){
        Connection cnn =null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql="delete from comments where tzid = ?";
            pstmt = cnn.prepareStatement(sql);
            int i = Integer.parseInt(tzId);
            pstmt.setInt(1,i);
            int n = pstmt.executeUpdate();
            cnn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if(cnn!=null) {
                try {
                    cnn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("出错，删除失败！");
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
    }

    /**
     * 通过评论的id，删除评论
     * @param id
     * @return
     */
    public boolean delById(String id){
        boolean flag = true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "delete from comments where id = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,Integer.parseInt(id));
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else{
                cnn.rollback();
                flag = false;
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
     * 通过帖子id，返回对应的评论信息
     * @return
     */
    public List<commentEntity> getBytzId(int tzId){
        List<commentEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from comments where tzid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,tzId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do{
                    commentEntity cm = new commentEntity();
                    cm.setId(rs.getInt("id"));
                    cm.setTexts(rs.getString("texts"));
                    cm.setuId(rs.getInt("uid"));
                    cm.setTzId(rs.getInt("tzid"));
                    cm.setPhoto(rs.getString("photo"));
                    list.add(cm);
                }while(rs.next());
            }else{
                list = null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            list=null;
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return list;
    }

    /**
     * 创建评论
     * @param cm
     * @return
     */
    public boolean build(commentEntity cm){
        boolean flag = true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "INSERT INTO comments VALUES(NULL,?,?,?,?);";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,cm.getTexts());
            pstmt.setInt(2,cm.getuId());
            pstmt.setInt(3,cm.getTzId());
            pstmt.setString(4,cm.getPhoto());
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else {
                cnn.rollback();
                flag = false;
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
            flag = false;
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
        return flag;
    }
}
