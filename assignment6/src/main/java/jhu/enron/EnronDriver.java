package jhu.enron;

import jhu.avro.EmailSimple;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

;


/**
 * Created by wilsopw1 on 2/25/17.
 */
public class EnronDriver extends Configured implements Tool {

    public int run(String[] strings) throws Exception {

        Configuration conf = getConf();
        String inputPath = conf.get("inputPath");
        String outputPath = conf.get("outputPath");
        Job job = Job.getInstance(conf, "Enron Processor");
        job.setJarByClass(getClass());

        String testMode = conf.get("testMode", "none");
        if(testMode.equals("none")) {
            System.out.printf("Enron Driver: %s %s\n", inputPath, outputPath);

            job.setMapperClass(EnronStatsMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setReducerClass(WCReducer.class);
            job.setCombinerClass(WCReducer.class);
            job.setNumReduceTasks(1);
        } else if(testMode.equals("avroTest")) {
            System.out.printf("Avro Test: %s %s\n", inputPath, outputPath);
            job.setMapperClass(EnronAvroMapper.class);
            AvroJob.setOutputKeySchema(job, EmailSimple.SCHEMA$);
            job.setOutputFormatClass(AvroKeyOutputFormat.class);
            job.setOutputValueClass(NullWritable.class);
            job.setNumReduceTasks(0);
        }

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        job.setInputFormatClass(AvroKeyInputFormat.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }
}
