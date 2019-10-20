package hdhw5.enron;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import javax.mail.internet.InternetAddress;

import java.io.IOException;
import java.util.List;

/**
 * Created by wilsopw1 on 2/25/17.
 */
public class EnronStatsMapper extends Mapper<LongWritable, Text, 
        Text, IntWritable> {
    Gson gson = new GsonBuilder().create();
    String inputPath = null;
    IntWritable One = new IntWritable(1);

    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
        // read configuration information
        this.inputPath = context.getConfiguration().get("inputPath");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {
        if(key.get() == 0) {
            // this is the first line of the file
            // Which files are we processing?
            String filename = ((FileSplit) context.getInputSplit())
                    .getPath().getName();
            context.getCounter("FILE", filename).increment(1);
        }

        // Deserialize json value into EmailMessage object
        EmailMessage message = gson.fromJson(value.toString(), 
                EmailMessage.class);
        
        //Iterate over message header keys
        for(String k : message.header.keySet()) {
            
            //Check if it's the from header and it's a valid email address
            if(k.equals("From") && isValid((String) message.header.get(k)))
                
                //Write mapper sender count
                context.write(new Text((String)message.header.get(k)), One);
            
            //Check if it's the to, cc, or bcc header, then get the receiver
            //email address list
            if(k.equals("To") || k.equals("Cc") || k.equals("Bcc")) {
                
                //Get receiver email address list
                List<String> emails = (List<String>)message.header.get(k);
                
                //Iterate over receiver email address list
                for(String e : emails)
                    
                    //Check if receiver address is valid
                    if (isValid(e))
                        
                        //Write mapper receiver count
                        context.write(new Text(e), One);
            }

        }


    }

    private boolean isValid(String emailAddress) {
        //Initialize result boolean variable
        boolean result = true;
        try {
            //Check email address is valid
            InternetAddress emailAddr = new InternetAddress(emailAddress);
            
        } catch (Exception ex) {
            //Update result boolean to report invalid email address format
            result = false;
        }
        
        //Return email address format validity
        return result;
    }
}
