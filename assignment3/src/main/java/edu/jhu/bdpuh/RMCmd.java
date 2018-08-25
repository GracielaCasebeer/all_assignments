package edu.jhu.bdpuh;

import org.apache.commons.cli.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;


/**
 *
 * Usage: hadoop jar target/<jar-file> <main-class> -rm -[<option>]  <src>
 *
 * Supports the following command and arguments -rm [-f] [-r|-R] [-skipTrash] <src>
 *
 *
 */
public class RMCmd {

    private FileSystem hdfs;
    private String[] args;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();


    public RMCmd(FileSystem hdfs, String[] args){

        this.hdfs = hdfs;
        this.args = args;
        options.addOption("rm", false, "Will not display a diagnostic message or modify the exit "
                + "status to reflect an error if the file does not exist.");
        options.addOption("f", false, "Will not display a diagnostic message or modify the exit "
                +"status to reflect an error if the file does not exist.");
        options.addOption("r", false, "Deletes the directory and any content under it recursively.");
        options.addOption("R", false, "Equivalent to -r");
        options.addOption("skipTrash", false, "Bypass trash, if enabled, and delete the specified "
                + "file(s) immediately. This can be useful when it is necessary to delete files from an over-quota directory.");
    }



    public void execute() throws IOException, ParseException {
        CommandLine line = parser.parse(options, args);
        System.out.println("rm cmd detected parsing options..");
        if (line.hasOption("f")) System.out.println("has -f option");
        if (line.hasOption("r")) System.out.println("has -r option");
        if (line.hasOption("R")) System.out.println("has -R option");
        if (line.hasOption("skipTrash")) System.out.println("has -skipTrash option");

        boolean isRemoved =  hdfs.delete(new Path(args[args.length-1]), (line.hasOption("r") || line.hasOption("R")));
        if(!isRemoved && !line.hasOption("f")) System.out.println("rm: '"+args[args.length-1]+"': No such file or directory");

        if(isRemoved && line.hasOption("skipTrash")) hdfs.delete(new Path(".Trash/Current"),true);

    }


}

