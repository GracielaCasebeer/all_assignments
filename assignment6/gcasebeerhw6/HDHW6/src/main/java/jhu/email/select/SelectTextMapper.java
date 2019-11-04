package jhu.email.select;

import static jhu.email.select.ArgumentUtil.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;
import jhu.avro.EmailSimple;
import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

class SelectTextMapper extends Mapper<AvroKey<EmailSimple>, NullWritable, 
        AvroKey<EmailSimple>, NullWritable> {
    
    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
    }
    
    @Override
    protected void map(AvroKey<EmailSimple> key, NullWritable value, 
            Context context) throws IOException, InterruptedException {
        //Get configuration
        Configuration conf = context.getConfiguration();
        //Get email datum
        EmailSimple email = key.datum();
        if (conf.get("pattern") != null && conf.get("type").equals("subject")) {
            if (email.getSubject() != null) {
                if (email.getSubject().toString().matches(conf.get("pattern"))) {
                    context.write(key, null);
                }
            }
        }
        if (conf.get("pattern") != null && conf.get("type").equals("body")) {
            if (email.getBody() != null) {
                if (email.getBody().toString().matches(conf.get("pattern"))) {
                    context.write(key, null);
                }
            }            
        }
    }
}
