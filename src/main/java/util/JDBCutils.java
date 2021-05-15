package util;

import java.sql.*;

public class JDBCutils {
    private static String url="jdbc:mysql://localhost:3306/qg_tieba";
    private static String user="root";
    private static String psw="root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,psw);
    }

    public static void Close(Connection cnn, Statement stmt){
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(cnn!=null){
            try {
                cnn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public static void Close(Connection cnn, Statement stmt, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        Close(cnn,stmt);
    }
}
