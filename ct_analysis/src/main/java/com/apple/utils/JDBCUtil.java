package com.apple.utils;

import java.sql.*;

/**
 * @Program: telecom-CustomerService
 * @ClassName: JDBCUtil
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-08 14:30
 * @Version 1.1.0
 **/
public class JDBCUtil {

    private static final String mysql_driver_class = "com.mysql.jdbc.Driver";
    private static final String mysql_url = "jdbc:mysql://master:3306/db_telecom?useSSL=false&createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8";
    private static final String mysql_username = "ubuntu";
    private static final String mysql_password = "123456";

    /**
     * 实例化JDBC连接对象
     *
     * @return
     */
    public static Connection getConnection() {
        try {
            Class.forName(mysql_driver_class);
            return DriverManager.getConnection(mysql_url, mysql_username, mysql_password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 释放连接器资源
     *
     * @param conn
     * @param ps
     * @param rs
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {

        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}