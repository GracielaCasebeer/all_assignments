package hdhw5.WG;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class WGDriver extends Configured implements Tool {
    
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String inputPath = conf.get("inputPath");
        String outputPath = conf.get("outputPath");

        System.out.printf("Weighted Graph Driver: %s %s\n", 
                inputPath, outputPath);
        Job job = Job.getInstance(conf, "Weighted Graph");
        job.setJarByClass(getClass());

        job.setMapperClass(WGMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FreqWritable.class);
        job.setCombinerClass(WGReducer.class);
        job.setReducerClass(WGReducer.class);        
        job.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        return job.waitForCompletion(true) ? 0 : 1;
    }
    
}
