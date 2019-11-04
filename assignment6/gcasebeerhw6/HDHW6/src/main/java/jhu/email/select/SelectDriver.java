package jhu.email.select;

import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class SelectDriver extends Configured implements Tool {

    public SelectDriver() {
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String type = conf.get("type");
        String inputPath = conf.get("inputPath");
        String outputPath = conf.get("outputPath");
        String pattern = conf.get("pattern");
        String startDate = conf.get("start");
        String endDate = conf.get("end");
        String from = conf.get("from");
        String to = conf.get("to");
        String cc = conf.get("cc");
        String logicalOper = conf.get("logicalOper");

        Job job = Job.getInstance(conf, "Email Select");
        job.setJarByClass(getClass());
        
        if (type.equals("time")) {
            System.out.printf("Email Select Driver:\n "
                    + "startDate %s\n endDate %s\n %s\n %s\n", 
                    startDate, endDate, inputPath, outputPath);
            job.setMapperClass(SelectTimeMapper.class);
        } else if (type.equals("address")) {
            System.out.printf("Email Select Driver:\n "
                    + "from %s\n to %s\n cc %s\n logicalOper %s\n %s\n %s\n", 
                    from, to, cc, logicalOper, inputPath, outputPath);
            job.setMapperClass(SelectAddressMapper.class);
        } else if (type.equals("subject")) {
            System.out.printf("Email Select Driver:\n "
                    + "subject pattern %s\n %s\n %s\n", pattern, inputPath, outputPath);
            job.setMapperClass(SelectTextMapper.class);
        } else if (type.equals("body")) {
            System.out.printf("Email Select Driver:\n "
                    + "body pattern %s\n %s\n %s\n", pattern, inputPath, outputPath);
            job.setMapperClass(SelectTextMapper.class);
        }
        
        job.setMapOutputKeyClass(AvroKeyInputFormat.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(0);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        job.setInputFormatClass(AvroKeyInputFormat.class);
        return job.waitForCompletion(true) ? 0 : 1;
    }
    
}

