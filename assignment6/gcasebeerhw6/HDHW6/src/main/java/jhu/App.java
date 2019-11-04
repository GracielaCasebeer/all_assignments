package jhu;

import jhu.enron.EmailMessage2;
import jhu.email.histogram.HistogramDriver;
import jhu.email.select.SelectDriver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import static jhu.email.select.ArgumentUtil.*;

public class App extends Configured implements Tool
{
    public static void main( String[] args ) throws Exception {
        int ret = ToolRunner.run(new Configuration(), new App(), args);
        System.exit(ret);
    }
    
    //type(1) + opt(4)-val(4) + input-output(1)
    private int runEnronEmailSelect(String type, String opt1, String val1,
            String opt2, String val2, String opt3, String val3, 
            String opt4, String val4, String input, String output) 
            throws Exception {
        Configuration conf = getConf();
        conf.set("type", type);
        conf.set(opt1.substring(1), val1);
        conf.set(opt2.substring(1), val2);
        conf.set(opt3.substring(1), val3);
        conf.set(opt4.substring(1), val4);
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new SelectDriver(), new String[]{});
    }
    
    //type(1) + opt(3)-val(3) + input-output(1)
    private int runEnronEmailSelect(String type, String opt1, String val1, 
            String opt2, String val2, String opt3, String val3, 
            String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("type", type);
        conf.set(opt1.substring(1), val1);
        conf.set(opt2.substring(1), val2);
        conf.set(opt3.substring(1), val3);
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new SelectDriver(), new String[]{});
    }
        
    //type(1) + opt(2)-val(2) + input-output(1)
    private int runEnronEmailSelect(String type, String opt1, String val1, 
            String opt2, String val2, String input, String output) 
            throws Exception {
        Configuration conf = getConf();
        conf.set("type", type);
        conf.set(opt1.substring(1), val1);
        conf.set(opt2.substring(1), val2);
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new SelectDriver(), new String[]{});
    }
    
    //type(1) + opt(1)-val(1) + input-output(1)
    private int runEnronEmailSelect(String type, String opt, String optValue, 
            String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("type", type);
        conf.set(opt.substring(1), optValue);
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new SelectDriver(), new String[]{});
    }

    private int runEnronEmailHistogram(String binType, String input, 
            String output) throws Exception {
        Configuration conf = getConf();
        conf.set("binType", binType);
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new HistogramDriver(), new String[]{});
    }

    int runEnron(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new EnronDriver(), new String[]{});
    }

    int writeToAvro(String input, String output) throws IOException {
        FileInputStream fis = new FileInputStream(input);
        DatumWriter<EmailSimple> dr = 
                new SpecificDatumWriter<>(EmailSimple.class);
        DataFileWriter<EmailSimple> dfw = new DataFileWriter<>(dr);
        dfw.create(EmailSimple.SCHEMA$, new File(output));

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        Gson gson = new GsonBuilder().create();

        String line = null;
        int total = 0;
        int dateErrors = 0;
        while ((line = br.readLine()) != null) {
            EmailMessage email = gson.fromJson(line, EmailMessage.class);
            EmailMessage2 email2 = gson.fromJson(line, EmailMessage2.class);
            EmailSimple emailSimple;
            try {
                emailSimple = email.getEmailSimple();
            } catch (Exception e) {
                emailSimple = email2.getEmailSimple();
            }            
            if(emailSimple.getDate() == 0L)
                dateErrors++;
            dfw.append(emailSimple);
            total++;
            if((total%1000)==0)
                System.out.printf("%6d records\r",total);
        }
        System.out.printf("%6d records total. %d date parsing errors\n",total, 
                dateErrors);
        dfw.close();
        br.close();

        return 0;
    }

    int runEnronTest(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("testMode", "avroTest");
        return runEnron(input, output);
    }


    public int run(String[] strings) throws Exception {
        //Initialize error message variable
        String errorMsg = "";

        //Convert strings to ArrayList
        ArrayList<String> args = new ArrayList<>(Arrays.asList(strings));
        
        //Check if user provided command arguments
        //Format: COMMAND TYPE <OPTIONS> INPUTPATH OUTPUTPATH
        if (args.size() > 0) {
            //Check enron-avro commands
            if (args.contains("enron-avro") && args.size() == 3) {
                return writeToAvro(args.get(1), args.get(2));
            }
            //Check enron-stats command
            if (args.contains("enron-stats") && args.size() == 3) {
                return runEnron(args.get(1), args.get(2));
            }
            //Check avro-map command
            if (args.contains("avro-map") && args.size() == 3) {
                return runEnron(args.get(1), args.get(2));
            }
            //Check email-histogram command
            if (args.contains("email-histogram") 
                    && (args.contains("hour") || args.contains("day") 
                        || args.contains("month") || args.contains("year")) 
                    && args.size() == 4) {
                return runEnronEmailHistogram(args.get(1),  //binType
                        args.get(2), args.get(3));          //input output
            }
            //Check email-select command
            if (args.contains("email-select") && args.contains("time")) {
                if (args.size() == 8) {
                    if (args.contains("-start") && args.contains("-end")) {
                        if (isValidDate(args.get(3)) 
                                && isValidDate(args.get(5))) {
                            LocalDate start = getDate(args.get(3));
                            LocalDate end = getDate(args.get(5));
                            if (start.compareTo(end) >= 0) {
                                errorMsg = "Error: Start date must be before "
                                        + "end date.";
                            } else {
                                return runEnronEmailSelect(args.get(1), //type
                                args.get(2), args.get(3),       //opt1 val1
                                args.get(4), args.get(5),       //opt2 val2
                                args.get(6), args.get(7));      //input output
                            }
                        } else {
                            errorMsg = "Error: Invalid date formatting.";
                        }
                    }
                }
                if (args.size() == 6) {
                    if (args.contains("-start") ^ args.contains("-end")) {
                        if (isValidDate(args.get(3))) {
                            return runEnronEmailSelect(args.get(1), //type
                            args.get(2), args.get(3),           //opt1 val1
                            args.get(4), args.get(5));          //input output
                        } else {
                            errorMsg = "Error: Invalid date formatting.";
                        }
                    }
                }
            }
            if (args.contains("email-select") && args.contains("address")) {
                //all 3 options provided: from, to, cc
                if (args.size() == 10 || args.size() == 11) {
                    if (args.contains("-from") 
                            && args.contains("-to") 
                            && args.contains("-cc")) {
                        if (isValidRegex(args.get(3)) 
                                &&  isValidRegex(args.get(5)) 
                                && isValidRegex(args.get(7))) {
                            if (args.size() == 11 && args.contains("-and")) {
                                return runEnronEmailSelect(args.get(1), //type
                                    args.get(2), args.get(3),       //opt1 val1
                                    args.get(4), args.get(5),       //opt2 val2
                                    args.get(6), args.get(7),       //opt3 val3
                                    "-logicalOper", args.get(8),    //opt4 val4
                                    args.get(9), args.get(10));     //in out
                            } else if (args.size() == 10) {
                                return runEnronEmailSelect(args.get(1), //type
                                    args.get(2), args.get(3),       //opt1 val1
                                    args.get(4), args.get(5),       //opt2 val2
                                    args.get(6), args.get(7),       //opt3 val3
                                    args.get(8), args.get(9));      //in out
                            }
                        } else {
                            errorMsg = "Error: Invalid regex";
                        }
                    }
                }
                //2 of 3 options provided from&to, from&cc, to&cc
                if (args.size() == 8 || args.size() == 9) {
                    if ((args.contains("-from") && args.contains("-to"))
                            || (args.contains("-from") && args.contains("-cc"))
                            || (args.contains("-to") && args.contains("-cc"))) {
                        if (isValidRegex(args.get(3)) 
                                &&  isValidRegex(args.get(5))) {
                            if (args.size() == 9 && args.contains("-and")) {
                                return runEnronEmailSelect(args.get(1),  //type
                                    args.get(2), args.get(3),       //opt1 val1
                                    args.get(4), args.get(5),       //opt2 val2
                                    "-logicalOper", args.get(6),    //opt3 val3
                                    args.get(7), args.get(8));      //in out
                            } else if (args.size() == 8) {
                                return runEnronEmailSelect(args.get(1), //type
                                    args.get(2), args.get(3),       //opt1 val1
                                    args.get(4), args.get(5),       //opt2 val2
                                    args.get(6), args.get(7));       //in out
                            }
                        } else {
                            errorMsg = "Error: Invalid regex";
                        }
                    }
                }
                //1 of 3 options provided from, to, cc
                if (args.size() == 6) {
                    if (args.contains("-from") 
                            ^ args.contains("-to") 
                            ^ args.contains("-cc")) {
                        if (isValidRegex(args.get(3))) {
                            return runEnronEmailSelect(args.get(1), //type
                                args.get(2), args.get(3),           //opt val
                                args.get(4), args.get(5));          //in out
                        } else {
                            errorMsg = "Error: Invalid regex";
                        }
                    }
                }
            }            
            if (args.contains("email-select") 
                    && (args.contains("subject") || args.contains("body")) 
                    && args.size() == 5) {
                if (isValidRegex(args.get(2))) {
                    return runEnronEmailSelect(args.get(1),     //type
                            "-pattern", args.get(2),            //opt val
                            args.get(3), args.get(4));          //input output
                } else {
                    errorMsg = "Error: Invalid regex";
                }
            }
        }
        
        showUsage(errorMsg);

        return -1;
    }

    void showUsage(String errorMsg) {
        StringBuilder usage = new StringBuilder();
        usage.append("\nUsage: \n");
        usage.append("\tenron-avro <localInputPath> <localOutputPath>\n");
        usage.append("\t\ttranslate json to avro EmailSimple record\n");
        usage.append("\tenron-stats <inputPath> <outputPath>\n");
        usage.append("\t\trun the enron statistics mapreduce job\n");
        usage.append("\tavro-map <inputPath> <outputPath>\n");
        usage.append("\t\trun the avro map only job\n");
        usage.append("\temail-histogram <bin-type> <inputPath> <outputPath>\n");
        usage.append("\t\trun the email histogram mapreduce job\n");
        usage.append("\t\t<bin-type> can be one of [hour, day, month, year]\n");
        usage.append("\temail-select <type> <options> ");
        usage.append("<inputPath> <outputPath>\n");
        usage.append("\t\trun the email select mapreduce job\n");
        usage.append("\t\ttime:\n");
        usage.append("\t\t   email-select time -start <date> -end <date>\n");
        usage.append("\t\taddress:\n");
        usage.append("\t\t   email-select address -from <pattern> ");
        usage.append("-to <pattern> -cc <pattern> -and\n");
        usage.append("\t\tsubject:\n");
        usage.append("\t\t   email-select subject <pattern>\n");
        usage.append("\t\tbody:\n");
        usage.append("\t\t   email-select body <pattern>\n");
        usage.append(errorMsg).append("\n");
        
        System.out.println(usage.toString());
    }
}
