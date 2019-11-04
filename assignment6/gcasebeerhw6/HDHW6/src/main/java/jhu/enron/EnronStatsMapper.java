package jhu.enron;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jhu.avro.EmailSimple;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wilsopw1 on 2/25/17.
 */
public class EnronStatsMapper extends Mapper<AvroKey<EmailSimple>, NullWritable,
        Text, IntWritable> {
    Gson gson = new GsonBuilder().create();
    String inputPath = null;
    IntWritable One = new IntWritable(1);

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(AvroKey<EmailSimple> key, NullWritable value, Context context) throws IOException, InterruptedException {

        EmailSimple email = key.datum();
        CharSequence from = email.getFrom();
        if(from != null)
            context.write(new Text(from.toString()), One);
        List<CharSequence> to = email.getTo();
        if(to != null)
            for(CharSequence e : to)
                context.write(new Text(e.toString()), One);
        List<CharSequence> cc = email.getCc();
        if(cc != null)
            for(CharSequence e : cc)
                context.write(new Text(e.toString()), One);
        List<CharSequence> bcc = email.getBcc();
        if(bcc != null)
            for (CharSequence e : bcc)
                context.write(new Text(e.toString()), One);
    }
}