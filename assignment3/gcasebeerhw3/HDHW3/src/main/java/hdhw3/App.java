package hdhw3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class App extends Configured implements Tool{
    private lsCmnd lsCmnd;
    private catCmnd catCmnd;
    private getCmnd getCmnd;
    private rmCmnd rmCmnd;
    
    public static void main(String[] args) throws Exception {
        //Intantiate hadoop configuration and main app objects
        Configuration conf = new Configuration();
        App app = new App();
        
        //Let ToolRunner handle generic command-line options
        int ret = ToolRunner.run(conf, app, args);
        
        //Exit system reporting runner error results
        System.exit(ret);
    }

    @Override
    public int run(String[] args) throws Exception {
        
        //Check number of arguments supplied by user
        if (args.length == 0) {
            //Incorrect number of arguments supplied by user
            printUsage();
            return -1;
        }
        
        //Get hadoop configuration
        Configuration conf = getConf();
        
        //Get hdfs file system
        FileSystem hdfs = FileSystem.get(conf);
        
        //Get local file system
        LocalFileSystem localfs = LocalFileSystem.getLocal(conf);
                
        //Instantiate command objects
        lsCmnd = new lsCmnd(hdfs, args);
        catCmnd = new catCmnd(hdfs, args);
        getCmnd = new getCmnd(localfs,hdfs, args);
        rmCmnd = new rmCmnd(hdfs, args);
        
        switch(args[0]) {
            case "-ls":
                lsCmnd.execute();
                break;
            case "-cat":
                catCmnd.execute();
                break;
            case "-get":
                getCmnd.execute();
                break;
            case "-rm":
                rmCmnd.execute();
                break;
            default:
                //printUsage();
                throw new UnsupportedOperationException("Unsupported command: "
                        + "[" + args[0] + "] "
                        + "Commands supported: [-ls, -cat, -get, -rm]");
        }
        return 0;   
    }
    
    private void printUsage() {
        System.out.println("Error: Arguments missing.");
        System.out.println("Commands supported: -ls, -cat, -get, -rm");
        System.out.println("Please enter a supported command.");
    }

}

