package hdhw5.DC;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class DegWritable implements Writable {
    private IntWritable inDeg;
    private IntWritable outDeg;
    
    public DegWritable() {
        this.inDeg = new IntWritable(0);
        this.outDeg = new IntWritable(0);
    }
    
    public DegWritable(IntWritable inDeg, IntWritable outDeg) {
        this.inDeg = inDeg;
        this.outDeg = outDeg;
    }

    public IntWritable getInDeg() {
        return inDeg;
    }

    public void setInDeg(IntWritable inDeg) {
        this.inDeg = inDeg;
    }

    public IntWritable getOutDeg() {
        return outDeg;
    }

    public void setOutDeg(IntWritable outDeg) {
        this.outDeg = outDeg;
    }

    @Override
    public void write(DataOutput d) throws IOException {
        inDeg.write(d);
        outDeg.write(d);
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        inDeg.readFields(di);
        outDeg.readFields(di);
    }
    
    @Override
    public String toString() {
        return (inDeg.toString() + "\t" + outDeg.toString());
    }
}
