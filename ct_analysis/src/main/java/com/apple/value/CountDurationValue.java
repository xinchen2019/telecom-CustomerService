package com.apple.value;

import com.apple.kv.base.BaseValue;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Program: telecom-CustomerService
 * @ClassName: CountDurationValue
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-08 19:39
 * @Version 1.1.0
 **/
public class CountDurationValue extends BaseValue {

    private String callSum;
    private String callDurationSum;

    public CountDurationValue() {
        super();
    }

    public String getCallSum() {
        return callSum;
    }

    public void setCallSum(String callSum) {
        this.callSum = callSum;
    }

    public String getCallDurationSum() {
        return callDurationSum;
    }

    public void setCallDurationSum(String callDurationSum) {
        this.callDurationSum = callDurationSum;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(callSum);
        out.writeUTF(callDurationSum);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.callSum = in.readUTF();
        this.callDurationSum = in.readUTF();
    }
}
