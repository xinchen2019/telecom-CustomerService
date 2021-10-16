package com.apple.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Program: telecom-CustomerService
 * @ClassName: ProductLog
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-10 11:02
 * @Version 1.1.0
 **/
public class ProductLog {
    private String startTime = "2017-01-01";
    private String endTime = "2017-12-31";

    private List<String> phoneList = new ArrayList<String>();
    private Map<String, String> phoneNameMap = new HashMap<String, String>();

    public static void main(String[] args) {

        String logPath = "data\\calllog.csv";
        args = new String[]{
                logPath
        };
        if (args == null || args.length <= 0) {
            System.out.println("no arguments");
            return;
        }

        ProductLog productLog = new ProductLog();
        productLog.initPhone();
        productLog.writeLog(args[0]);
    }

    public void initPhone() {
        phoneList.add("17078388295");
        phoneList.add("13980337439");
        phoneList.add("14575535933");
        phoneList.add("19902496992");
        phoneList.add("18549641558");
        phoneList.add("17005930322");
        phoneList.add("18468618874");
        phoneList.add("18576581848");
        phoneList.add("15978226424");
        phoneList.add("15542823911");
        phoneList.add("17526304161");
        phoneList.add("15422018558");
        phoneList.add("17269452013");
        phoneList.add("17764278604");
        phoneList.add("15711910344");
        phoneList.add("15714728273");
        phoneList.add("16061028454");
        phoneList.add("16264433631");
        phoneList.add("17601615878");
        phoneList.add("15897468949");

        phoneNameMap.put("17078388295", "李雁");
        phoneNameMap.put("13980337439", "卫艺");
        phoneNameMap.put("14575535933", "仰莉");
        phoneNameMap.put("19902496992", "陶欣悦");
        phoneNameMap.put("18549641558", "施梅梅");
        phoneNameMap.put("17005930322", "金虹霖");
        phoneNameMap.put("18468618874", "魏明艳");
        phoneNameMap.put("18576581848", "华贞");
        phoneNameMap.put("15978226424", "华啟倩");
        phoneNameMap.put("15542823911", "仲采绿");
        phoneNameMap.put("17526304161", "卫丹");
        phoneNameMap.put("15422018558", "戚丽红");
        phoneNameMap.put("17269452013", "何翠柔");
        phoneNameMap.put("17764278604", "钱溶艳");
        phoneNameMap.put("15711910344", "钱琳");
        phoneNameMap.put("15714728273", "缪静欣");
        phoneNameMap.put("16061028454", "焦秋菊");
        phoneNameMap.put("16264433631", "吕访琴");
        phoneNameMap.put("17601615878", "沈丹");
        phoneNameMap.put("15897468949", "褚美丽");
    }

    /**
     * 形式：15837312345,13737312345,2017-01-09 08:09:10,0360
     *
     * @return
     */
    public String product() {
        String caller = null;
        String callee = null;

        String callerName = null;
        String calleeName = null;

        int callerIndex = (int) (Math.random() * phoneList.size());
        caller = phoneList.get(callerIndex);
        callerName = phoneNameMap.get(caller);
        while (true) {
            //取得被叫电话号码
            int calleeIndex = (int) (Math.random() * phoneList.size());
            callee = phoneList.get(calleeIndex);
            calleeName = phoneNameMap.get(callee);
            if (!caller.equals(callee)) {
                break;
            }
        }

        String buildTime = randomBuildTime(startTime, endTime);
        //0000
        DecimalFormat df = new DecimalFormat("0000");
        String duration = df.format((int) (30 * 60 * Math.random()));
        StringBuilder sb = new StringBuilder();
        sb.append(caller + ",").append(callee + ",").append(buildTime + ",").append(duration);
        writeToKafka(sb.toString());
        return sb.toString();
    }

    /**
     * 根据传入的时间区间，在此范围内随机通话建立的时间
     * startTimeTS + (endTimeTs - startTimeTs) * Math.random();
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private String randomBuildTime(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf1.parse(startTime);
            Date endDate = sdf1.parse(endTime);

            if (endDate.getTime() <= startDate.getTime()) {
                return null;
            }

            long randomTs = (startDate.getTime() + (long) ((endDate.getTime() - startDate.getTime()) * Math.random()));

            Date resultDate = new Date(randomTs);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String resultFormatString = sdf2.format(resultDate);
            return resultFormatString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将数据写入到文件中
     *
     * @param filePath
     */
    public void writeLog(String filePath) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
            while (true) {
                Thread.sleep(500);
                String log = product();
                System.out.println(log);
                osw.write(log + "\n");
                osw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void writeToKafka(String str) {
        String brokerList = "master:9092,slave1:9092;slave2:9092";
        KafkaProducer<String, String> producer;
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        //所有follower都响应了才认为消息提交成功，即"committed"
        props.put("acks", "all");
        //retries = MAX 无限重试，直到你意识到出现了问题:)
        props.put("retries", 0);
        //producer将试图批处理消息记录，以减少请求次数.默认的批量处理消息字节数
        //batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
        props.put("batch.size", 16384);
        //延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，
        // producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
        props.put("linger.ms", 1);
        //producer可以用来缓存数据的内存大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
        producer.send(new ProducerRecord<String, String>("calllog", str));
    }
}

