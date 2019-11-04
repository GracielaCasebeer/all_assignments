package jhu.email.histogram;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jhu.avro.EmailSimple;
import static jhu.email.select.ArgumentUtil.*;
import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

class HistogramMapper extends Mapper<AvroKey<EmailSimple>, NullWritable,
        Text, IntWritable> {
    
    IntWritable One = new IntWritable(1);
    
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }
    
    @Override
    protected void map(AvroKey<EmailSimple> key, NullWritable value, 
            Context context) throws IOException, InterruptedException {
        //Get configuration
        Configuration conf = context.getConfiguration();
        //Get email datum
        EmailSimple email = key.datum();
        //Get email date
        LocalDateTime emailDate = getDateTime(email.getDate());
        //Get year, month, day, hour values from email date
        String year = Integer.toString(emailDate.getYear());
        String month = Integer.toString(emailDate.getMonthValue());
        String day = Integer.toString(emailDate.getDayOfMonth());
        String hour = Integer.toString(emailDate.getHour());
        
        
        if (conf.get("binType") != null) {
            if (conf.get("binType").equals("year")) {
                context.write(new Text(year), One);
            }
            if (conf.get("binType").equals("month")) {
                context.write(new Text(year + "." + month), One);
            }
            if (conf.get("binType").equals("day")) {
                context.write(new Text(year + "." + month + "." + day), One);
            }
            if (conf.get("binType").equals("hour")) {
                context.write(new Text(year + "." + month + "." + day 
                        + "." + hour), One);
            }
        }
    }
}
