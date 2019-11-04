package jhu.enron;

import jhu.avro.EmailSimple;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by hduser on 3/8/17.
 */
public class EmailMessage {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z (z)");
    String body;
    Map<String,String> header;
    String file;

    public EmailSimple getEmailSimple() {
        EmailSimple.Builder builder = EmailSimple.newBuilder();
        
        // process the header date value
        if(header.containsKey("Date")) {
            String date = header.get("Date");
            try {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, formatter);
                builder.setDate(zonedDateTime.toEpochSecond());
            } catch (Exception e) {
                //System.out.printf("Error parsing date\n");
                builder.setDate(0L);
            }
        }
        
        // process the header from value
        if(header.containsKey("From")) {
            builder.setFrom(header.get("From").trim());
        }  else {
            builder.setFrom(null);
        }
        
        //Initialize variables
        String[] emails = null;
        String regex = "\\s*,\\s*";
        
        // process the header to value
        if(header.containsKey("To")) {
            emails = header.get("To").trim().split(regex);
            builder.setTo(Arrays.<CharSequence>asList(emails));
        } else {
            builder.setTo(null);
        }
        
        // process the header cc value
        if(header.containsKey("Cc")) {
            emails = header.get("Cc").trim().split(regex);
            builder.setCc(Arrays.<CharSequence>asList(emails));
        } else {
            builder.setCc(null);
        }
        
        // process the header bcc value
        if(header.containsKey("Bcc")) {
            emails = header.get("Bcc").trim().split(regex);
            builder.setBcc(Arrays.<CharSequence>asList(emails));
        } else {
            builder.setBcc(null);
        }
        
        // process the header subject value
        if(header.containsKey("Subject")) {
            builder.setSubject(header.get("Subject").trim());
        } else {
            builder.setSubject(null);
        }
        
        // process the body
        if (!body.isEmpty()) {
            builder.setBody(body);
        }
            
        return builder.build();
    }
}
