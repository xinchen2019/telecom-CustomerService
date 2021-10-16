package com.apple.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Program: telecom-CustomerService
 * @ClassName: JDBCInstance
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-08 14:50
 * @Version 1.1.0
 **/
public class JDBCInstance {
    private static Connection conn = null;

    private JDBCInstance() {
    }

    public static Connection getInstance() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = JDBCUtil.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
