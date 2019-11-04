package jhu.email.select;

import java.io.IOException;
import java.util.List;
import jhu.avro.EmailSimple;
import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class SelectAddressMapper extends Mapper
        <AvroKey<EmailSimple>, NullWritable, 
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
        //Get email from data
        String emailFrom = email.getFrom().toString();
        //Get email to data
        List<CharSequence> to = email.getTo();
        //Get email cc data
        List<CharSequence> cc = email.getCc();
        if (conf.get("logicalOper") == null) {
            //Check pattern match
            if ((emailFrom != null) && (conf.get("from") != null)) {
                if (emailFrom.matches(conf.get("from"))) {
                    context.write(key, null);
                }
            }
            //Check pattern match
            if ((to != null) && (conf.get("to") != null)) {
                for (CharSequence item : to) {
                    if (item.toString().matches(conf.get("to"))) {
                        context.write(key, null);
                    }
                }
            }
            //Check pattern match
            if ((cc != null) && (conf.get("cc") != null)) {
                for (CharSequence item : cc) {
                    if (item.toString().matches(conf.get("cc"))) {
                        context.write(key, null);
                    }
                }
            }
        } else {
            if ((conf.get("from") != null) 
                    && (conf.get("to") != null) && (conf.get("cc") != null)) {
                if ((emailFrom != null) && (to != null) && (cc != null)) {
                    if (emailFrom.matches(conf.get("from"))) {
                        for (CharSequence itemTo : to) {
                            if (itemTo.toString().matches(conf.get("to"))) {
                                for (CharSequence itemCc : cc) {
                                    if (itemCc.toString()
                                            .matches(conf.get("cc"))) {
                                        context.write(key, null);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }                
            } 
            if ((conf.get("from") != null) && (conf.get("to") != null) 
                    && (conf.get("cc") == null)) {
                if ((emailFrom != null) && (to != null)) {
                    if (emailFrom.matches(conf.get("from"))) {
                        for (CharSequence itemTo : to) {
                            if (itemTo.toString().matches(conf.get("to"))) {
                                context.write(key, null);
                                break;
                            }
                        }
                    }
                }
            }
            if ((conf.get("from") != null) && (conf.get("to") == null) 
                    && (conf.get("cc") != null)) {
                if ((emailFrom != null) && (cc != null)) {
                    if (emailFrom.matches(conf.get("from"))) {
                        for (CharSequence itemCc : cc) {
                            if (itemCc.toString().matches(conf.get("cc"))) {
                                context.write(key, null);
                                break;
                            }
                        }
                    }
                }
            }
            if ((conf.get("from") == null) && (conf.get("to") != null) 
                    && (conf.get("cc") != null)) {
                if ((to != null) && (cc != null)) {
                    for (CharSequence itemTo : to) {
                        if (itemTo.toString().matches(conf.get("to"))) {
                            for (CharSequence itemCc : cc) {
                                if (itemCc.toString().matches(conf.get("cc"))) {
                                    context.write(key, null);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
