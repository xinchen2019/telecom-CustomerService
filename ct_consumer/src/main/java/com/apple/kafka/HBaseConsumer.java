package com.apple.kafka;

import com.apple.hbase.HBaseDAO;
import com.apple.utils.PropertiesUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;

/**
 * @Program: telecom-CustomerService
 * @ClassName: HBaseConsumer
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-10 12:47
 * @Version 1.1.0
 **/
public class HBaseConsumer {
    public static void main(String[] args) {
        System.setProperty("HADOOP_USER_NAME", "ubuntu");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(PropertiesUtil.properties);
        kafkaConsumer.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));
        HBaseDAO hd = new HBaseDAO();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> cr : records) {
                String oriValue = cr.value();
                System.out.println(oriValue);
                hd.put(oriValue);
            }
        }
    }
}
