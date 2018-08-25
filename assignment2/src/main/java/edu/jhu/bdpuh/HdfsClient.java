package edu.jhu.bdpuh;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * A simple HDFS Client
 *
 */
public class HdfsClient extends Configured implements Tool
{
    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello HDFS!" );

        int ret = ToolRunner.run(new Configuration(), new HdfsClient(), args);
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
        if(strings.length == 0) {
            printUsage();
            return -1;
        }
  
        if(strings[0].equals("-lsr"))
            listFiles(hdfs, strings);
        else {
            printUsage();
            return -1;
        }

        return 0;
    }
}
