package jhu.enron;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jhu.avro.EmailSimple;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroWrapper;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by wilsopw1 on 2/25/17.
 */
public class EnronAvroMapper extends Mapper<AvroKey<EmailSimple>, NullWritable,
        AvroWrapper<EmailSimple>, NullWritable> {
    Gson gson = new GsonBuilder().create();
    String inputPath = null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(AvroKey<EmailSimple> key, NullWritable value, Context context) throws IOException, InterruptedException {

        EmailSimple email = key.datum();
        context.write(new AvroKey<EmailSimple>(email), NullWritable.get());
    }
}