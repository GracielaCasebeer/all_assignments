package hdhw5.WG;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class FreqWritable implements Writable {
    private IntWritable toFreq;
    private IntWritable ccFreq;
    
    public FreqWritable() {
        this.toFreq = new IntWritable(0);
        this.ccFreq = new IntWritable(0);
    }
    
    public FreqWritable(IntWritable toFreq, IntWritable ccFreq) {
        this.toFreq = toFreq;
        this.ccFreq = ccFreq;
    }

    public IntWritable getToFreq() {
        return toFreq;
    }

    public void setToFreq(IntWritable toFreq) {
        this.toFreq = toFreq;
    }

    public IntWritable getCcFreq() {
        return ccFreq;
    }

    public void setCcFreq(IntWritable ccFreq) {
        this.ccFreq = ccFreq;
    }
    
    @Override
    public void write(DataOutput d) throws IOException {
        toFreq.write(d);
        ccFreq.write(d);
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        toFreq.readFields(di);
        ccFreq.readFields(di);
    }
    
    @Override
    public String toString() {
        return (toFreq.toString() + "\t" + ccFreq.toString());
    }
}
