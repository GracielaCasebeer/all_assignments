package jhu;

import jhu.searchindex.SearchDriver;
import jhu.wordcount.WordDriver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class App extends Configured implements Tool
{
    public static void main( String[] args ) throws Exception {        
        int ret = ToolRunner.run(new Configuration(), new App(), args);
        System.exit(ret);
    }    

    public int run(String[] strings) throws Exception {
        if(strings.length > 0) {
            //wordcount action requested
            if (strings[0].equals("wordcount") && strings.length == 3) {
                return runWordCount(strings[1], strings[2]);
            }
            //searchindex action requested
            if (strings[0].equals("searchindex") && strings.length == 3) {
                return runSearchIndex(strings[1], strings[2]);
            }
        }        
        showUsage();
        return -1;
    }
    
    int runWordCount(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new WordDriver(), new String[]{});
    }
    
    private int runSearchIndex(String input, String output) throws Exception {
        Configuration conf = getConf();
        conf.set("inputPath", input);
        conf.set("outputPath", output);
        return ToolRunner.run(conf, new SearchDriver(), new String[]{});
    }
    
    void showUsage() {
        System.out.println("\nUsage: [wordcount|searchindex] "
                + "<inputPath> <outputPath>");
    }
}
