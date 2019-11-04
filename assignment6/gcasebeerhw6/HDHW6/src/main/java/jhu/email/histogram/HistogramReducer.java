package jhu.email.histogram;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

class HistogramReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
    }
    
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, 
            Context context) throws IOException, InterruptedException {
        int sum = 0;
        for(IntWritable i : values) {
            sum += i.get();
        }
        context.write(new Text(key), new IntWritable(sum));
    }
}
