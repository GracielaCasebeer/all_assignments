package jhu;

import jhu.wordcount.WCDriver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

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

    int runWordCount(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new WCDriver(), new String[]{});
    }

    void showUsage() {
        System.out.println("Usage: ");
        System.out.println("\twordcount <inputPath> <outputPath>\trun the wordcount mapreduce job");
    }

    public int run(String[] strings) throws Exception {
        if(strings.length > 0) {
            if (strings[0].equals("wordcount") && strings.length == 3) {
                return runWordCount(strings[1], strings[2]);
            }
        }
        showUsage();

        return -1;
    }
}
