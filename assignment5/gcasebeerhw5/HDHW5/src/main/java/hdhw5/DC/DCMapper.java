package hdhw5.DC;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class DCMapper extends Mapper<LongWritable, Text, Text, DegWritable> {
    String inputPath = null;
    IntWritable One = new IntWritable(1);
    
    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
        // read configuration information
        this.inputPath = context.getConfiguration().get("inputPath");
    }
    
    @Override
    protected void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {
        if(key.get() == 0) {
            // this is the first line of the file
            // Which files are we processing?
            String filename = ((FileSplit) context.getInputSplit())
                    .getPath().getName();
            context.getCounter("FILE", filename).increment(1);
        }
        
        //Convert line into String array
        String[] words = value.toString().split("\\s+");
        
        //Declare DegWritable object
        DegWritable degrees = null;
        
        //Initialize sender email
        String sender = words[0].trim().toLowerCase();
        
        //Instantiate DegWritable object and update in/out degree values
        degrees = new DegWritable(new IntWritable(0), new IntWritable(1));
        
        //Write mapper output
        context.write(new Text(sender), degrees);
        
        //Initialize receiver email
        String receiver = words[1].trim().toLowerCase();
        
        //Istantiate DegWritable object and update in/out degree values
        degrees = new DegWritable(new IntWritable(1), new IntWritable(0));
        
        //Write mapper output
        context.write(new Text(receiver), degrees);
        
    }
}
