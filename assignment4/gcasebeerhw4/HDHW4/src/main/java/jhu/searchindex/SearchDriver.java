package jhu.searchindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;


public class SearchDriver extends Configured implements Tool {

    public int run(String[] strings) throws Exception {
        
        //Get configuration
        Configuration conf = getConf();
        //Initialize path variables
        String inputPath = conf.get("inputPath");
        String outputPath = conf.get("outputPath");
        
        //Info message
        System.out.printf("Search Count: %s %s\n", inputPath, outputPath);
        
        //Create a new job
        Job job = Job.getInstance(conf, "Search Count");
        job.setJarByClass(getClass());
        
        //Set input and output format
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        
        //Set job-specific parameters - Mapper, Combiner, and Reducer
        job.setMapperClass(SearchMapper.class);
        //job.setCombinerClass(SearchReducer.class);
        job.setReducerClass(SearchReducer.class);
        
        //Set output key and value classes        
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CustomWriter.class);
        
        //Set number or reducers
        job.setNumReduceTasks(1);        

        return job.waitForCompletion(true) ? 0 : 1;
    }
}
