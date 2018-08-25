package edu.jhu.bdpuh;

import org.apache.commons.cli.*;
import org.apache.hadoop.fs.FileSystem;

import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;



/**
 *
 * Usage: hadoop jar target/<jar-file> <main-class> -cat -[<option>] <path> <src>
 *
 * Supports the following command and arguments -cat [-ignoreCrc]  [<src> ...]
 *
 *
 */
public class CATCmd {

    private FileSystem hdfs;
    private String[] args;
    private LocalFileSystem localFileSystem;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();


    public CATCmd(FileSystem hdfs, String[] args, LocalFileSystem localFileSystem){

        this.hdfs = hdfs;
        this.args = args;
        this.localFileSystem = localFileSystem;
        options.addOption("cat", false, "Copies source paths to stdout");
        options.addOption("ignoreCrc", false, "Disables checksum verification.");

    }

    private void catFile(Path path) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(hdfs.open(path)));
        try {
            String line;
            line=br.readLine();
            while (line != null){
                System.out.println(line);
                line = br.readLine();
            }
        } finally {
            br.close();
        }

    }

    public void execute() throws IOException, ParseException {
        CommandLine line = parser.parse(options, args);
        System.out.println("cat cmd detected parsing options..");
        if (line.hasOption("ignoreCrc")) System.out.println("has -ignoreCrc option");
        localFileSystem.setVerifyChecksum(!line.hasOption("ignoreCrc"));
        catFile(new Path(args[args.length-1]));


    }


}
