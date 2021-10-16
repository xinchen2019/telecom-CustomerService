package com.apple.reducer;

import com.apple.kv.key.ComDimension;
import com.apple.value.CountDurationValue;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Program: telecom-CustomerService
 * @ClassName: CountDurationReducer
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-08 20:15
 * @Version 1.1.0
 **/
public class CountDurationReducer extends Reducer<ComDimension, Text, ComDimension, CountDurationValue> {

    CountDurationValue countDurationValue = new CountDurationValue();

    @Override
    protected void reduce(ComDimension key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        int callsum = 0;
        int callDurationSum = 0;
        for (Text t : values) {
            callsum++;
            callDurationSum += Integer.valueOf(t.toString());
        }
        countDurationValue.setCallSum(String.valueOf(callsum));
        countDurationValue.setCallDurationSum(String.valueOf(callDurationSum));
        context.write(key, countDurationValue);
    }
}
