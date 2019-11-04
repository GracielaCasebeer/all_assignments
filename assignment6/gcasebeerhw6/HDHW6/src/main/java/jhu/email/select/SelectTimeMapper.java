package jhu.email.select;

import static jhu.email.select.ArgumentUtil.*;
import java.io.IOException;
import java.time.LocalDate;
import jhu.avro.EmailSimple;
import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class SelectTimeMapper extends Mapper<AvroKey<EmailSimple>, NullWritable, 
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
        //Declare variables
        LocalDate start = null;
        LocalDate end = null;
        //Get email datum
        EmailSimple email = key.datum();
        //Get email date
        LocalDate emailDate = getDate(email.getDate());            
        //Get start date argument
        if (conf.get("start") != null) {
            start = getDate(conf.get("start"));
        }
        //Get end date argument
        if (conf.get("end") != null) {
            end = getDate(conf.get("end"));
        }
        if (start != null && end != null) {
            if ((emailDate.isAfter(start) || emailDate.isEqual(start)) 
                    && (emailDate.isBefore(end) || emailDate.isEqual(end))) {
                context.write(key, null);
            }
        } else if (start != null) {
            if (emailDate.isAfter(start) || emailDate.isEqual(start)) {
                context.write(key, null);
            }
        } else if (end != null) {
            if (emailDate.isBefore(end) || emailDate.isEqual(end)) {
                context.write(key, null);
            }
        }
    }
}
