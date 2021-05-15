package dao;

import com.fasterxml.jackson.databind.exc.InvalidNullException;
import entity.followTb;
import entity.followTz;
import entity.tieziEntity;
import util.JDBCutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作与关注贴吧或帖子有关的数据库的类
 */
public class FollowDao {
    /**
     * 添加用户关注的贴吧
     * @param ftb
     * @return
     */
    public boolean addtb(followTb ftb){
        boolean flag = true;
        if(checkTb(ftb)){                               //数据库中没有要录入的关注贴吧
            Connection cnn = null;
            PreparedStatement pstmt = null;
            try {
                cnn = JDBCutils.getConnection();
                cnn.setAutoCommit(false);
                String sql = "insert into follow_tb values(null,?,?);";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setInt(1,ftb.getuId());
                pstmt.setInt(2,ftb.getTbId());
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
                flag = false;
            }finally {
                JDBCutils.Close(cnn,pstmt);
            }
        }else{
            flag = false;
        }
        return flag;
    }

    /**
     * 判断数据库里，是否已经用户关注的贴吧
     * @param ftb
     * @return
     */
    public static boolean checkTb(followTb ftb){
        boolean flag = true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="select * from follow_tb where uid = ? and tbid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,ftb.getuId());
            pstmt.setInt(2,ftb.getTbId());
            rs = pstmt.executeQuery();
            if(rs.next()){                      //若存在关注的贴吧
                flag = false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return flag;
    }

    /**
     * 添加用户收藏的帖子
     * @param ftz
     * @return
     */
    public boolean addtz(followTz ftz){
        boolean flag = true;
        if(checkTz(ftz)){
            Connection cnn = null;
            PreparedStatement pstmt = null;
            try {
                cnn = JDBCutils.getConnection();
                cnn.setAutoCommit(false);
                String sql = "insert into follow_tz values(null,?,?);";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setInt(1,ftz.getuId());
                pstmt.setInt(2,ftz.getTzId());
                int i = pstmt.executeUpdate();
                if(i==1){
                    cnn.commit();
                }else{
                    cnn.rollback();
                    flag = false;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                if (cnn != null) {
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
        }else{
            flag = false;
        }
        return flag;
    }

    /**
     * 判断数据库里，是否已经有用户收藏的帖子
     * @param ftz
     * @return
     */
    public static boolean checkTz(followTz ftz){
        boolean flag = true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from follow_tz where uid = ? and tzid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,ftz.getuId());
            pstmt.setInt(2,ftz.getTzId());
            rs = pstmt.executeQuery();
            if(rs.next()){                      //数据库存在用户收藏的帖子
                flag = false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            flag = false;
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return flag;
    }

    /**
     * 根据用户id，返回用户关注贴吧的数据
     * @param uId
     * @return
     */
    public List<followTb> getByUid_Tb(int uId){
        List<followTb> list=new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "SELECT * FROM follow_tb where uid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,uId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    followTb ftb = new followTb();
                    ftb.setId(rs.getInt("id"));
                    ftb.setuId(rs.getInt("uid"));
                    ftb.setTbId(rs.getInt("tbid"));
                    list.add(ftb);
                }while(rs.next());
            }else {
                list = null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return list;
    }

    /**
     * 根据贴吧id和用户id，删除用户关注的贴吧
     * @param tbId
     * @param uId
     */
    public void deleteTb(String tbId,int uId){
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "DELETE FROM follow_tb WHERE tbid = ? and uid = ?; ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,Integer.parseInt(tbId));
            pstmt.setInt(2,uId);
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else{
                cnn.rollback();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                cnn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
    }

    /**
     * 根据用户id，返回用户关注的帖子信息
     * @param uId
     * @return
     */
    public List<tieziEntity> getByUid_Tz(int uId){
        List<tieziEntity>   list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from follow_tz where uid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,uId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do{
                    int tzid = rs.getInt("tzid");
                    String tzId = Integer.toString(tzid);
                    tieziEntity tz = new TieziDao().getByid(tzId);
                    list.add(tz);
                }while(rs.next());
            }else{
                list = null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            list = null;
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return list;
    }

    /**
     * 删除数据库里，用户收藏的帖子
     * @param tzId
     * @param uId
     */
    public void deleteTz(String tzId,int uId){
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "DELETE FROM follow_tz WHERE uid = ? AND tzid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,uId);
            pstmt.setInt(2,Integer.parseInt(tzId));
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else{
                cnn.rollback();
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
    }
}
