package jhu.email.histogram;

import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class HistogramDriver extends Configured implements Tool {

    public HistogramDriver() {
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String type = conf.get("binType");
        String inputPath = conf.get("inputPath");
        String outputPath = conf.get("outputPath");
        
        Job job = Job.getInstance(conf, "Email Histogram");
        job.setJarByClass(getClass());
        
        System.out.printf("Email Histogram Driver: \n binType %s\n %s\n %s\n"
                , type, inputPath, outputPath);
        
        job.setMapperClass(HistogramMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setReducerClass(HistogramReducer.class);
        job.setCombinerClass(HistogramReducer.class);
        job.setNumReduceTasks(1);
        
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        job.setInputFormatClass(AvroKeyInputFormat.class);
        
        return job.waitForCompletion(true) ? 0 : 1;
    }
    
}
