package com.gy.fileserver.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnUtil {

    public static Connection getConnection(){//获取数据库连接
        Connection connection = null;
        Properties properties = new Properties();
        InputStream in = new DBConnUtil().getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            properties.load(in);
            String driverClass = properties.getProperty("driver");
            String url = properties.getProperty("url");
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void release(ResultSet rs, PreparedStatement ps, Connection con) {//释放数据库连接
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
