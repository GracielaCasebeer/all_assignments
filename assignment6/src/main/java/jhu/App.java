package jhu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jhu.avro.EmailSimple;
import jhu.enron.EmailMessage;
import jhu.enron.EnronDriver;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.*;

import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

/**
 * Programming Assignment 2: MapReduce Introduction
 *
 */
public class App extends Configured implements Tool
{
    public static void main( String[] args ) throws Exception {
        int ret = ToolRunner.run(new Configuration(), new App(), args);
        System.exit(ret);
    }

    int writeToAvro(String input, String output) throws IOException {
        FileInputStream fis = new FileInputStream(input);
        DatumWriter<EmailSimple> dr = new SpecificDatumWriter<EmailSimple>(EmailSimple.class);
        DataFileWriter<EmailSimple> dfw = new DataFileWriter<EmailSimple>(dr);
        dfw.create(EmailSimple.SCHEMA$, new File(output));

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        Gson gson = new GsonBuilder().create();

        String line = null;
        int total = 0;
        int dateErrors = 0;
        while ((line = br.readLine()) != null) {
            EmailMessage email = gson.fromJson(line, EmailMessage.class);
            EmailSimple emailSimple = email.getEmailSimple();
            if(emailSimple.getDate() == 0L)
                dateErrors++;
            dfw.append(emailSimple);
            total++;
            if((total%1000)==0)
                System.out.printf("%6d records\r",total);
        }
        System.out.printf("%6d records total. %d date parsing errors\n",total, dateErrors);
        dfw.close();
        br.close();

        return 0;
    }

    int runEnronTest(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("testMode", "avroTest");
        return runEnron(input, output);
    }

    int runEnron(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new EnronDriver(), new String[]{});
    }

    void showUsage() {
        System.out.println("Usage: ");
        System.out.println("\tenron-avro <localInputPath> <localOutputPath>\ttranslate json to avro EmailSimple record");
        System.out.println("\tenron-stats <inputPath> <outputPath>\trun the enron statistics mapreduce job");
        System.out.println("\tavro-map <inputPath> <outputPath>\trun the avro map only job");
    }

    public int run(String[] strings) throws Exception {
        if(strings.length > 0) {
            if (strings[0].equals("enron-stats") && strings.length == 3) {
                return runEnron(strings[1], strings[2]);
            } else if(strings[0].equals("enron-avro") && strings.length == 3) {
                return writeToAvro(strings[1], strings[2]);
            } else if(strings[0].equals("avro-map") && strings.length == 3) {
                return runEnronTest(strings[1],strings[2]);
            }
        }
        showUsage();

        return -1;
    }
}
