package dao;

import entity.tieziEntity;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import util.JDBCutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TieziDao {

    /**
     * 创建帖子
     * @param tz1
     * @return
     */
    public boolean createTiezi(tieziEntity tz1){
        boolean flag=true;
        PreparedStatement pstmt = null;
        Connection cnn = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql="INSERT INTO tiezi VALUES(NULL,?,?,0,0,?,?);";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,tz1.getName());
            pstmt.setString(2,tz1.getTexts());
            pstmt.setInt(3,tz1.getuId());
            pstmt.setInt(4,tz1.getTbId());
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
     * 获取数据库中全部帖子的数据，并封装在list集合中
     * @return
     */
    public List<tieziEntity> tieziData(){
        List<tieziEntity> list =new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="SELECT * FROM tiezi;";
            pstmt = cnn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                tieziEntity tz = new tieziEntity();
                tz.setId(rs.getInt("id"));
                tz.setName(rs.getString("name"));
                tz.setTexts(rs.getString("texts"));
                tz.setNumber(rs.getInt("number"));
                tz.setLikes(rs.getInt("likes"));
                tz.setuId(rs.getInt("uid"));
                tz.setTbId(rs.getInt("tbid"));
                list.add(tz);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
        return list;
    }

    /**
     * 删除帖子
     * @param id
     */
    public void delete(String id){
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql="DELETE FROM tiezi WHERE id = ?;";
            pstmt = cnn.prepareStatement(sql);
            int i = Integer.parseInt(id);
            pstmt.setInt(1,i);
            int n = pstmt.executeUpdate();
            if(n==1){
                cnn.commit();
            }else {
                cnn.rollback();
                System.out.println("删除帖子失败！");
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
            System.out.println("出错！删除评论失败！");
        }finally {
            JDBCutils.Close(cnn,pstmt);
        }
    }

    /**
     * 通过id，返回帖子的信息
     * @param id
     * @return
     */
    public tieziEntity getByid(String id){
        tieziEntity tz = new tieziEntity();
        Connection cnn = null;
        ResultSet rs = null;
                PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql="select * from tiezi where id = ?;";
            pstmt = cnn.prepareStatement(sql);
            int i = Integer.parseInt(id);
            pstmt.setInt(1,i);
            rs = pstmt.executeQuery();
            rs.next();
            //封装数据
            tz.setId(i);
            tz.setName(rs.getString("name"));
            tz.setTexts(rs.getString("texts"));
            tz.setNumber(rs.getInt("number"));
            tz.setLikes(rs.getInt("likes"));
            tz.setuId(rs.getInt("uid"));
            tz.setTbId(rs.getInt("tbid"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            tz=null;
        }finally {
            JDBCutils.Close(cnn,pstmt,rs);
        }
        return tz;
    }

    /**
     * 返回某个贴吧下所有的帖子
     * @param tbId
     * @return
     */
    public List<tieziEntity> getByTbId(int tbId){
        List<tieziEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from tiezi where tbid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,tbId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    tieziEntity tz = new tieziEntity();
                    tz.setId(rs.getInt("id"));
                    tz.setName(rs.getString("name"));
                    tz.setTexts(rs.getString("texts"));
                    tz.setNumber(rs.getInt("number"));
                    tz.setLikes(rs.getInt("likes"));
                    tz.setuId(rs.getInt("uid"));
                    tz.setTbId(rs.getInt("tbid"));
                    list.add(tz);
                }while(rs.next());
            }else{
                list = null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            list = null;
        }finally {

        }
        return list;
    }

    /**
     * 更新帖子的点赞数和评论数
     * @param tz
     */
    public void update(tieziEntity tz){
        Connection cnn = null;
        PreparedStatement pstmt = null;
        try {
            cnn = JDBCutils.getConnection();
            cnn.setAutoCommit(false);
            String sql = "UPDATE tiezi SET number = ? ,likes = ?  WHERE id = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,tz.getNumber());
            pstmt.setInt(2,tz.getLikes());
            pstmt.setInt(3,tz.getId());
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
        }
    }

    /**
     * 通过用户的id，返回帖子信息
     * @return
     */
    public List<tieziEntity> getByUid(int uId){
        List<tieziEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "select * from tiezi where uid = ?;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,uId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                do{
                    tieziEntity tz = new tieziEntity();
                    tz.setId(rs.getInt("id"));
                    tz.setName(rs.getString("name"));
                    tz.setTexts(rs.getString("texts"));
                    tz.setNumber(rs.getInt("number"));
                    tz.setLikes(rs.getInt("likes"));
                    tz.setuId(rs.getInt("uid"));
                    tz.setTbId(rs.getInt("tbid"));
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
     * 模糊查询
     * @param name
     * @return
     */
    public List<tieziEntity> search(String name,int tbId){
        List<tieziEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "SELECT * FROM tiezi WHERE NAME LIKE ? AND tbid = ? ;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setString(1,name);
            pstmt.setInt(2,tbId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    tieziEntity tz = new tieziEntity();
                    tz.setId(rs.getInt("id"));
                    tz.setName(rs.getString("name"));
                    tz.setTexts(rs.getString("texts"));
                    tz.setNumber(rs.getInt("number"));
                    tz.setLikes(rs.getInt("likes"));
                    tz.setuId(rs.getInt("uid"));
                    tz.setTbId(rs.getInt("tbid"));
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
     * 按评论数量，降序排序
     * @param tbId
     * @return
     */
    public List<tieziEntity> sortByComments(int tbId){
        List<tieziEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "SELECT *  FROM tiezi WHERE tbid = ? ORDER BY number DESC ;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,tbId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    tieziEntity tz = new tieziEntity();
                    tz.setId(rs.getInt("id"));
                    tz.setName(rs.getString("name"));
                    tz.setTexts(rs.getString("texts"));
                    tz.setNumber(rs.getInt("number"));
                    tz.setLikes(rs.getInt("likes"));
                    tz.setuId(rs.getInt("uid"));
                    tz.setTbId(rs.getInt("tbid"));
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
     * 按点赞数量，降序排序
     * @param tbId
     * @return
     */
    public List<tieziEntity> sortByLikes(int tbId){
        List<tieziEntity> list = new ArrayList<>();
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            cnn = JDBCutils.getConnection();
            String sql = "SELECT *  FROM tiezi WHERE tbid = ? ORDER BY likes DESC ;";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setInt(1,tbId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                do {
                    tieziEntity tz = new tieziEntity();
                    tz.setId(rs.getInt("id"));
                    tz.setName(rs.getString("name"));
                    tz.setTexts(rs.getString("texts"));
                    tz.setNumber(rs.getInt("number"));
                    tz.setLikes(rs.getInt("likes"));
                    tz.setuId(rs.getInt("uid"));
                    tz.setTbId(rs.getInt("tbid"));
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
}
