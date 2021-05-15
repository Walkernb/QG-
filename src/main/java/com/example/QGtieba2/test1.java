package com.example.QGtieba2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.TiebaDao;
import dao.TieziDao;
import entity.tiebaEntity;
import entity.tieziEntity;
import org.junit.Test;
import util.JDBCutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class test1 {
    @Test
    public void test() throws SQLException {
        Connection cnn = JDBCutils.getConnection();
        String sql="select * from user where id=1";
        PreparedStatement pstmt = cnn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        String photo = rs.getString("photo");
        System.out.println("photo——>"+photo);
        JDBCutils.Close(cnn,pstmt,rs);
    }

    @Test
    public void Json() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String str = "success";
        String s = mapper.writeValueAsString("success");
        System.out.println(s);
    }

    @Test
    public void search(){
        List<tiebaEntity> search = new TiebaDao().search("%广工%");
        System.out.println(search);
    }

    @Test
    public void C_sort(){
        List<tieziEntity> list = new TieziDao().sortByComments(5);
        System.out.println(list);
    }

    @Test
    public void L_sort(){
        List<tieziEntity> list = new TieziDao().sortByLikes(5);
        System.out.println(list);
    }
}
