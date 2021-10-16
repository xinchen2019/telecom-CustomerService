package com.apple.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Program: telecom-CustomerService
 * @ClassName: PropertiesUtil
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-10 10:53
 * @Version 1.1.0
 **/
public class PropertiesUtil {

    public static Properties properties = null;

    static {
        InputStream is = ClassLoader.getSystemResourceAsStream("hbase_consumer.properties");
        properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
