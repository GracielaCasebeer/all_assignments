package hdhw5.WG;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WGReducer extends Reducer<Text, FreqWritable, Text, FreqWritable> {
    
    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<FreqWritable> values, 
            Context context) throws IOException, InterruptedException {
        
        //Declare and initialize to & cc sum variables
        int toSum = 0;
        int ccSum = 0;
        
        //Iterate over mapper values
        for(FreqWritable f : values) {
            
            //Update to & sum variables
            toSum += f.getToFreq().get();
            ccSum += f.getCcFreq().get();
        }
        
        //Write reducer output
        context.write(new Text(key), new FreqWritable(
                new IntWritable(toSum), new IntWritable(ccSum)));
    }
}
