package hdhw5;

import hdhw5.DC.DCDriver;
import hdhw5.WG.WGDriver;
import hdhw5.enron.EnronDriver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
//import org.apache.log4j.PropertyConfigurator;

/**
 * Programming Assignment 2: MapReduce Introduction
 *
 */
public class App extends Configured implements Tool
{
    public static void main( String[] args ) throws Exception {
        //PropertyConfigurator.configure("/etc/hadoop/log4j.properties");
        int ret = ToolRunner.run(new Configuration(), new App(), args);
        System.exit(ret);
    }

    public int run(String[] strings) throws Exception {
        if(strings.length > 0) {
            if (strings[0].equals("enron-stats") && strings.length == 3) {
                return runEnron(strings[1], strings[2]);
            }
            if (strings[0].equals("weighted-graph") && strings.length == 3) {
                return runWG(strings[1], strings[2]);
            }
            if (strings[0].equals("degree-centrality") && strings.length == 3) {
                return runDC(strings[1], strings[2]);
            }
        }
        showUsage();

        return -1;
    }

    int runEnron(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new EnronDriver(), new String[]{});
    }
    
    private int runWG(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new WGDriver(), new String[]{});
    }
    
    private int runDC(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new DCDriver(), new String[]{});
    }
    
    void showUsage() {
        System.out.println("Usage: ");
        System.out.println("\tenron-stats <inputPath> <outputPath>"
                + "\trun the enron statistics mapreduce job\n"
                + "\tweighted-graph <inputPath> <outputPath>"
                + "\trun the weighted graph mapreduce job\n"
                + "\tdegree-centrality <inputPath> <outputPath>"
                + "\trun the degree centrality mapreduce job\n");
    }

}
