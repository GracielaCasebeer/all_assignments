package edu.jhu.bdpuh;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Hello Hadoop world!
 *
 */
public class App extends Configured implements Tool
{

    private LSCmd lsCmd;
    private CATCmd catCmd;
    private GETCmd getCmd;
    private RMCmd rmCmd;

    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello Hadoop World!" );

        int ret = ToolRunner.run(new Configuration(), new App(), args);
        System.exit(ret);
    }

    void listFiles(FileSystem hdfs, String[] strings) throws IOException {
        System.out.println("list files from: "+hdfs.getUri().toString());
        RemoteIterator<LocatedFileStatus> listing = hdfs.listFiles(new Path(strings[1]), true);
        while(listing.hasNext()) {
            LocatedFileStatus fs = listing.next();
            System.out.println(fs.getPath());
        }
    }

    void printUsage() {
        System.out.println("Usage: ");
        System.out.println("  -lsr <path> \t\trecursively list files on path");
    }

    public int run(String[] strings) throws Exception {
        FileSystem hdfs = FileSystem.get(getConf());
        LocalFileSystem localFileSystem = FileSystem.getLocal(getConf());

        lsCmd = new LSCmd(hdfs, strings);
        catCmd = new CATCmd(hdfs,strings, localFileSystem);
        getCmd = new GETCmd(hdfs, strings, localFileSystem);
        rmCmd = new RMCmd(hdfs, strings);

        if(strings.length == 0) {
            printUsage();
            return -1;
        }


        switch(strings[0]) {
            case "-lsr":
                listFiles(hdfs, strings);
                break;
            case "-mkdir":
                new MkdirCmd(strings);
                break;
            case "-ls":
                lsCmd.execute();
                break;
            case "-cat":
                catCmd.execute();
                break;
            case "-get":
                getCmd.execute();
                break;
            case "-rm":
                rmCmd.execute();
                break;
            default:
                printUsage();
                break;
        }
        return 0;
    }
}
