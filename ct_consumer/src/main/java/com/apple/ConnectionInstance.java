package com.apple;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * @Program: telecom-CustomerService
 * @ClassName: ConnectionInstance
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-09 23:51
 * @Version 1.1.0
 **/
public class ConnectionInstance {

    private static Connection conn;

    public static synchronized Connection getConnection(Configuration conf) {
        try {
            if (conn == null || conn.isClosed()) {
                conn = ConnectionFactory.createConnection(conf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }
}