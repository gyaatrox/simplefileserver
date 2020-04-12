package com.gy.fileserver;

import java.sql.*;

public class DerbyTest {
    private final static String DB_URL = "jdbc:derby:D:\\Program Files\\db-derby-10.11.1.1-bin\\bin\\filedb";
    private final static String DERBY_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName(DERBY_DRIVER);
            conn = DriverManager.getConnection(DB_URL);

            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM ijtest");

            while (result.next()) {
                System.out.println("序号 : " + result.getInt(1));
            }
            result.close();
            stat.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 内嵌模式数据库操作用完之后需要关闭数据库,这里没有执行数据库名称则全部关闭.
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                e.getMessage();
            }
        }
    }
}