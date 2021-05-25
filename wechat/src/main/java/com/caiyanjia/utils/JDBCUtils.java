package com.caiyanjia.utils;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {


    public static Connection getConnection() {

        try {
//            InputStream is1 = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            InputStream is1 = JDBCUtils.class.getResourceAsStream("/jdbc.properties");
            Properties pros = new Properties();

            pros.load(is1);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");


            Connection conn = null;

            Class.forName(driverClass);

            conn = DriverManager.getConnection(url, user, password);

            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

    }

    public static void closeResoure(Connection conn, java.sql.PreparedStatement ps, ResultSet rs) {

        try {
            ps.close();
            if (conn != null)
                conn.close();
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
