package jhu.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, 
            Context context) throws IOException, InterruptedException {
        //Initialize counter
        int sum = 0;
        
        for(IntWritable i : values) {
            //Increment counter
            sum += i.get();
        }
        
        context.write(key, new IntWritable(sum));
    }
}
