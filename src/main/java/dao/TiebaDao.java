package dao;

import entity.followTb;
import entity.tiebaEntity;
import util.JDBCutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TiebaDao {

    /**
     * 创建贴吧
     * @param tb1
     * @return
     */
    public boolean createTieba(tiebaEntity tb1){
        boolean flag=true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql="INSERT INTO tieba VALUES(NULL,?,0,?,?,'N');";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,tb1.getName());
            pstmt.setInt(2,tb1.getuId());
            pstmt.setString(3,tb1.getNotice());
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
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
        return flag;
    }

    /**
     * 获取并封装数据库里所有的贴吧数据
     * @return
     */
    public List<tiebaEntity> tiebaData(){
        List<tiebaEntity> list=new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="SELECT * FROM tieba;";
            pstmt = cnn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    if(rs.getString("status").equals("Y")){
                        tiebaEntity tb = new tiebaEntity();
                        tb.setId(rs.getInt("id"));
                        tb.setName(rs.getString("name"));
                        tb.setNumber(rs.getInt("number"));
                        tb.setuId(rs.getInt("uid"));
                        tb.setNotice(rs.getString("notice"));
                        tb.setStatus(rs.getString("status"));
                        list.add(tb);
                    }
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
     * 返回新创建贴吧的id
     * @param tb
     * @return
     */
    public int getId(tiebaEntity tb){
        int id=0;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="SELECT id FROM tieba WHERE NAME = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,tb.getName());
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
     * 修改贴吧公告
     * @param tb
     * @param notice
     * @return
     */
    public boolean alter(tiebaEntity tb,String notice){
        boolean flag=true;
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql="UPDATE tieba SET notice= ? WHERE id=?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,notice);
            pstmt.setInt(2,tb.getId());
            int i = pstmt.executeUpdate();
            if(i==1){
                cnn.commit();
            }else{
                cnn.rollback();
                flag=false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            flag=false;
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
     * 根据id，返回贴吧信息
     * @param id
     * @return
     */
    public tiebaEntity getById(int id){
        tiebaEntity tb = new tiebaEntity();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from tieba where id = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,id);
            rs = pstmt.executeQuery();
            rs.next();
            tb.setId(rs.getInt("id"));
            tb.setNotice(rs.getString("notice"));
            tb.setuId(rs.getInt("uid"));
            tb.setNumber(rs.getInt("number"));
            tb.setName(rs.getString("name"));
            tb.setStatus(rs.getString("status"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return tb;
    }

    /**
     * 返回用户创建的贴吧
     * @param uId
     * @return
     */
    public List<tiebaEntity> getByUid(int uId){
        List<tiebaEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from tieba where uid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,uId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    tiebaEntity tb = new tiebaEntity();
                    tb.setId(rs.getInt("id"));
                    tb.setName(rs.getString("name"));
                    tb.setNumber(rs.getInt("number"));
                    tb.setuId(rs.getInt("uid"));
                    tb.setNotice(rs.getString("notice"));
                    tb.setStatus(rs.getString("status"));
                    list.add(tb);
                }while(rs.next());
            }else {
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
     * 模糊查询
     * @param text
     * @return
     */
    public List<tiebaEntity> search(String text){
        List<tiebaEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "SELECT * FROM tieba WHERE NAME LIKE ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,text);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    if(rs.getString("status").equals("Y")){
                        tiebaEntity tb = new tiebaEntity();
                        tb.setId(rs.getInt("id"));
                        tb.setName(rs.getString("name"));
                        tb.setNumber(rs.getInt("number"));
                        tb.setuId(rs.getInt("uid"));
                        tb.setNotice(rs.getString("notice"));
                        tb.setStatus(rs.getString("status"));
                        list.add(tb);
                    }
                }while(rs.next());
            }else {
                list = null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            list = null;
        }finally{
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return list;
    }

    /**
     * 返回待审核的贴吧信息
     * @return
     */
    public List<tiebaEntity> getByStatus(){
        List<tiebaEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from tieba where status = 'N';";
            pstmt = cnn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    tiebaEntity tb = new tiebaEntity();
                    tb.setId(rs.getInt("id"));
                    tb.setName(rs.getString("name"));
                    tb.setNumber(rs.getInt("number"));
                    tb.setuId(rs.getInt("uid"));
                    tb.setNotice(rs.getString("notice"));
                    tb.setStatus(rs.getString("status"));
                    list.add(tb);
                }while(rs.next());
            }else {
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
     * 修改贴吧的status为 Y
     * @param id
     */
    public void agreeBuild(String id){
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "UPDATE tieba SET STATUS = 'Y' WHERE id = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,Integer.parseInt(id));
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
