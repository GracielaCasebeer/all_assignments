package jhu.enron;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;


/**
 * Created by wilsopw1 on 2/25/17.
 */
public class EnronDriver extends Configured implements Tool {

    public int run(String[] strings) throws Exception {

        Configuration conf = getConf();
        String inputPath = conf.get("inputPath");
        String outputPath = conf.get("outputPath");

        System.out.printf("Enron Driver: %s %s\n", inputPath, outputPath);
        Job job = Job.getInstance(conf, "Enron Processor");
        job.setJarByClass(getClass());

        job.setMapperClass(EnronStatsMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setReducerClass(WCReducer.class);
        job.setCombinerClass(WCReducer.class);
        job.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        return job.waitForCompletion(true) ? 0 : 1;
    }
}
