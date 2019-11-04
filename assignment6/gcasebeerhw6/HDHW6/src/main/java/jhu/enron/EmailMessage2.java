package jhu.enron;

import java.util.Arrays;
import jhu.avro.EmailSimple;

public class EmailMessage2 {
    Long date;
    String from;
    String[] to;
    String[] cc;
    String[] bcc;
    String subject;
    String body;
    public EmailSimple getEmailSimple() {
        EmailSimple.Builder builder = EmailSimple.newBuilder();
        
        // process date
        if (date != null) {
            builder.setDate(date);
        } else {
            builder.setDate(0L);
        }
        
        // process from
        if (from != null && !from.isEmpty()) {
            builder.setFrom(from);
        } else {
            builder.setFrom(null);
        }
        
        // process to
        if (to != null) {
            builder.setTo(Arrays.<CharSequence>asList(to));
        } else {
            builder.setTo(null);
        }
        
        //process cc
        if (cc != null) {
            builder.setCc(Arrays.<CharSequence>asList(cc));
        } else {
            builder.setCc(null);
        }
        
        //process bcc
        if (bcc != null) {
            builder.setBcc(Arrays.<CharSequence>asList(bcc));
        } else {
            builder.setBcc(null);
        }
        
        // process subject
        if (subject != null && !subject.isEmpty()) {
            builder.setSubject(subject);
        } else {
            builder.setSubject(null);
        }
        
        // process the body
        if (subject != null && !body.isEmpty()) {
            builder.setBody(body);
        } else {
            builder.setBody(null);
        }
        
        return builder.build();
        
    }
    
}
