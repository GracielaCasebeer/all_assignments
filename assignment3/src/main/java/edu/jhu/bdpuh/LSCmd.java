package edu.jhu.bdpuh;

import org.apache.commons.cli.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Usage: hadoop jar target/<jar-file> <main-class> -ls -[<option>] <path>
 *
 * Supports the following command and arguments -ls [-d] [-h] [-R] [<path> ...]
 *
 *
 */
public class LSCmd {

    private FileSystem hdfs;
    private String[] args;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();


    public LSCmd(FileSystem hdfs, String[] args){

        this.hdfs = hdfs;
        this.args = args;
        options.addOption("ls", false, "show all files and subdirectories in current directory");
        options.addOption("d", false, "show all subdirectories current directory");
        options.addOption("h",false, "create parent");
        options.addOption("R",false, "show all files and subdirectories recursively from starting path");

    }

    private void recursiveListing(Path path ,ArrayList<LocatedFileStatus> listing) throws IOException{

        RemoteIterator<LocatedFileStatus> iterator = hdfs.listLocatedStatus(path);
        while(iterator.hasNext()) {
            LocatedFileStatus fs = iterator.next();
            if(fs.isDirectory()){
                listing.add(fs);
                recursiveListing(fs.getPath(), listing);
            } else listing.add(fs);
        }
    }

    private void printListing(ArrayList<LocatedFileStatus> listing, boolean onlyDirs, boolean humanRead) {

        for(LocatedFileStatus fs : listing){
            if(onlyDirs){ // only print directories
                if(fs.isDirectory()){
                    System.out.println((fs.isDirectory() ? "d":"-")+fs.getPermission()+"\t"
                            + (fs.getReplication() == 0? "-": fs.getReplication())+" "+fs.getOwner()+" "+fs.getGroup() +"\t"
                            + (humanRead ? humanReadableFileSize(fs.getLen()) : fs.getLen())+" "+(new Date(fs.getModificationTime()))+"\t"
                            + fs.getPath());
                }
            } else{ // Print everything
                System.out.println((fs.isDirectory() ? "d":"-")+fs.getPermission()+"\t"
                        + (fs.getReplication() == 0? "-": fs.getReplication())+" "+fs.getOwner()+" "+fs.getGroup() +"\t"
                        + (humanRead ? humanReadableFileSize(fs.getLen()) : fs.getLen())+" "+(new Date(fs.getModificationTime()))+"\t"
                        + fs.getPath());
            }

        }
    }

    private String humanReadableFileSize(long size){

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;


        if(size < sizeMb)
            return df.format(size / sizeKb)+ " Kb";
        else if(size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if(size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "Very Very Big";
    }


    public void execute() throws IOException, ParseException {
        CommandLine line = parser.parse(options, args);
        System.out.println("ls cmd detected parsing options..");
        if (line.hasOption("R")) System.out.println("has -R option");
        if (line.hasOption("d")) System.out.println("has -d option");
        if (line.hasOption("h")) System.out.println("has -h option");

        ArrayList<LocatedFileStatus> listing = new ArrayList<LocatedFileStatus>();
        if (line.hasOption("R")){
            recursiveListing(new Path(args[args.length-1]),listing);
        } else{
            RemoteIterator<LocatedFileStatus> iterator = hdfs.listLocatedStatus(new Path(args[args.length-1]));
            while (iterator.hasNext()){
                listing.add(iterator.next());
            }
        }
        printListing(listing, line.hasOption("d"), line.hasOption("h"));
    }



}
