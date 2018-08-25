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
        // process the date
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
        if(header.containsKey("From")) {
            builder.setFrom(header.get("From").trim());
        }  else
            builder.setFrom(null);
        String[] emails = null;
        String regex = "\\s*,\\s*";
        if(header.containsKey("To")) {
            emails = header.get("To").trim().split(regex);
            builder.setTo(Arrays.<CharSequence>asList(emails));
        } else
            builder.setTo(null);
        if(header.containsKey("Cc")) {
            emails = header.get("Cc").trim().split(regex);
            builder.setCc(Arrays.<CharSequence>asList(emails));
        } else
            builder.setCc(null);
        if(header.containsKey("Bcc")) {
            emails = header.get("Bcc").trim().split(regex);
            builder.setBcc(Arrays.<CharSequence>asList(emails));
        } else
            builder.setBcc(null);
        return builder.build();
    }
}
