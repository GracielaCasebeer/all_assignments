package hdhw5.WG;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hdhw5.enron.EmailMessage;
import java.io.IOException;
import java.util.List;
import javax.mail.internet.InternetAddress;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class WGMapper extends Mapper<LongWritable, Text, Text, FreqWritable> {
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

        //Declare sender variable
        String sender = null;
        
        //Deserialize json value into EmailMessage object
        EmailMessage message = gson.fromJson(value.toString(), 
                EmailMessage.class);
        
        //Iterate over message header keys
        for(String k : message.header.keySet()) {
            
            //If it's the from header and it's a valid email address, then 
            //initialize the sender email address
            if(k.equals("From") && isValid((String) message.header.get(k))) {
                sender = (String) message.header.get(k);
            }
            
            //If it's the to or cc header and the sender had a valid email
            //address, then get the receiver email addresses list
            if((k.equals("To") || k.equals("Cc")) && isValid(sender)) {                
                
                //Get receiver email addresses list
                List<String> emailAddresses = 
                        (List<String>)message.header.get(k);
                
                //Delcare frequencies variable
                FreqWritable frequencies = null;
                
                //Iterate over receiver email addresses list
                for(String e : emailAddresses) {
                    
                    //Check if receiver address is valid
                    if (isValid(e)) {
                        
                        //Declare and initialize receiver counts
                        int toCount = 0;
                        int ccCount = 0;
                        
                        //Update "to" receiver count
                        if (k.equals("To")) {
                            toCount = 1;
                        }
                        
                        //Update "cc" receiver count
                        if (k.equals("Cc")) {
                            ccCount = 1;
                        }
                        
                        //Initialize frequencies custom writable variable
                        frequencies = new FreqWritable(new IntWritable(toCount), 
                                new IntWritable(ccCount));
                        
                        //Write mapper result
                        context.write(new Text(sender + "\t" + e), frequencies);
                        
                    }
                }
            }
        }
    }
    
    public boolean isValid(String emailAddress) {
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
