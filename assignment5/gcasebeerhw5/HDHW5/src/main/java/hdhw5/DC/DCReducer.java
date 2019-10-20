package hdhw5.DC;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DCReducer extends Reducer<Text, DegWritable, Text, DegWritable> {
    
    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
    }
    
    @Override
    protected void reduce(Text key, Iterable<DegWritable> values, 
            Context context) throws IOException, InterruptedException {
        
        //Declare and initialize in & out sum variables
        int inSum = 0;
        int outSum = 0;
        
        //Iterate over mapper values
        for(DegWritable d : values) {
            
            //Update in & out sum variables
            inSum += d.getInDeg().get();
            outSum += d.getOutDeg().get();
        }
        
        //Write reducer output
        context.write(new Text(key), new DegWritable(
                new IntWritable(inSum), new IntWritable(outSum)));
    }
}
