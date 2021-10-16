package com.apple.runner;

import com.apple.kv.key.ComDimension;
import com.apple.mapper.CountDurationMapper;
import com.apple.outputformat.MysqlOutputFormat;
import com.apple.reducer.CountDurationReducer;
import com.apple.value.CountDurationValue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @Program: telecom-CustomerService
 * @ClassName: CountDurationRunner
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-08 20:50
 * @Version 1.1.0
 **/
public class CountDurationRunner extends Configured implements Tool {
    String tableName = "ns_ct:calllog";
    //private Configuration conf = null;

    public static void main(String[] args) {
        System.setProperty("HADOOP_USER_NAME", "ubuntu");
        System.out.println();
        try {
            int status = 0;
            status = ToolRunner.run(new CountDurationRunner(), args);
            System.exit(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        //得到conf
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", "master,slave1,slave2");
        //实例化job
        Job job = Job.getInstance(conf);
        job.setJarByClass(CountDurationRunner.class);
        //组装Mapper InputFormat
        initHbaseInputConfig(tableName, job);
        //组装Reducer OutFormat
        initHbaseOutputConfig(job);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    private void initHbaseInputConfig(String tableName, Job job) {
        Connection conn = null;
        Admin admin = null;
        try {
            conn = ConnectionFactory.createConnection(job.getConfiguration());
            admin = conn.getAdmin();
            if (!admin.tableExists(TableName.valueOf(tableName))) {
                throw new RuntimeException("无法找到目标表.");
            }
            Scan scan = new Scan();
            //可以优化
            //初始化Mapper
            TableMapReduceUtil.initTableMapperJob(
                    tableName,
                    scan,
                    CountDurationMapper.class,
                    ComDimension.class,
                    Text.class,
                    job,
                    true
            );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (admin != null) {
                    admin.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param job
     */
    private void initHbaseOutputConfig(Job job) {
        job.setReducerClass(CountDurationReducer.class);
        job.setOutputKeyClass(ComDimension.class);
        job.setOutputValueClass(CountDurationValue.class);
        job.setOutputFormatClass(MysqlOutputFormat.class);
    }
}
