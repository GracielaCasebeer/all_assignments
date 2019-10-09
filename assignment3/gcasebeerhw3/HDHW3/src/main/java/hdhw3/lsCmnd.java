package hdhw3;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

public class lsCmnd {
    private FileSystem hdfs;
    private String[] args;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();
    
    public lsCmnd(FileSystem hdfs, String[] args) {
        this.hdfs = hdfs;
        this.args = args;
        options.addOption("ls", false, "List directory contents");
        options.addOption("d", false, "Directories are listed as plain files.");
        options.addOption("h", false, "Format file sizes in a human-readable "
                + "fashion (eg. 54.0m instead of 67108864).");
        options.addOption("R", false, "Recursively list subdirectories "
                + "encountered.");            
    }
    
    public void execute() {
        try {
            //Get options and arguments from command line
            CommandLine line = parser.parse(options, args);
            
            //Check if 
            if (line.getArgs().length == 0) {
                throw new MissingArgumentException("Argument missing.");
            }
            
            //Initialize boolean variables for -d and -h options
            boolean dirsOnly = false;
            boolean humanRead = false;
            
            if (line.getOptions().length > 1 ) {
                //Print command and options requested
                System.out.print("-ls command received with option: ");
                Option[] cmdOptions = line.getOptions();
                for (Option opt : cmdOptions) {
                    System.out.print("-" + opt.getOpt() + " ");
                }
                //Update dirsOnly if applicable
                if (line.hasOption("d")) dirsOnly = true;
                //Update humanRead if applicable
                if (line.hasOption("h")) humanRead = true;
                //Options -R and -d are mutually exlusive
                //Hence, checking if recursive listing actually applies
                if (line.hasOption("R") && !dirsOnly) {                    
                    //Get path
                    Path path = new Path(args[args.length-1]);
                    
                    //Intantiate array to hold directory content
                    ArrayList<LocatedFileStatus> fileList = 
                            new ArrayList<LocatedFileStatus>();
                    
                    printList(getListDirectoryRecursive(path, fileList), 
                            humanRead, dirsOnly);
                                        
                } else {
                    //User requested -d option. No recursive listing needed.
                    printList(getListDirectory(args), humanRead, dirsOnly);
                }
            } else if (line.getOptions().length == 1){                
                System.out.println("-ls command received with no options");
                
                printList(getListDirectory(args), humanRead, dirsOnly);
            }
        } 
        catch (MissingArgumentException ex) {
            Logger.getLogger(lsCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
            printUsage();
        }
        catch (ParseException | IOException ex) {
            Logger.getLogger(lsCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
        }        
    }
    
    private ArrayList<LocatedFileStatus> getListDirectory(String[] args) 
            throws IOException {
        //Get path
        Path path = new Path(args[args.length-1]);
        
        //Intantiate array to hold directory content
        ArrayList<LocatedFileStatus> fileList = 
                new ArrayList<LocatedFileStatus>();

        //Get iterator with list of statuses of the files/directories
        //in the specified path.
        RemoteIterator<LocatedFileStatus> iterator = 
                hdfs.listLocatedStatus(path);
        
        //Populate fileList to return
        while (iterator.hasNext()) {
            fileList.add(iterator.next());
        }        
        return fileList;
    }

    private ArrayList<LocatedFileStatus> getListDirectoryRecursive(Path path, 
            ArrayList<LocatedFileStatus> fileList) throws IOException {        
        //Get iterator with list of statuses of the files/directories
        //in the specified path.
        RemoteIterator<LocatedFileStatus> iterator = 
                hdfs.listLocatedStatus(path);
        
        //Populate fileList
        while (iterator.hasNext()) {
            LocatedFileStatus item = iterator.next();
            if (item.isDirectory()){
                fileList.add(item);
                getListDirectoryRecursive(item.getPath(), fileList);
            }
            else {
                fileList.add(item);
            }
        }        
            return fileList;
    }
    
    private void printList(ArrayList<LocatedFileStatus> fileList, 
            boolean humanRead, boolean dirsOnly) {
        //Print list directory
        System.out.println();
        for (LocatedFileStatus item : fileList) {
            //Check if "-d" option is enabled
            if (dirsOnly && !item.isDirectory()) {
                System.out.print("");
            } else {
                System.out.println(item.getPermission() + "\t" 
                    + (item.isDirectory() ? "d":"-") + "\t" 
                    + (humanRead ? humanGetLen(item.getLen()) : item.getLen()) 
                    + "\t" + new Date(item.getModificationTime()) + "\t" 
                    + item.getPath() + "\t" );
            }
        }
    }
    
    private String humanGetLen(long fileSize) {        
        DecimalFormat df = new DecimalFormat("0.00");
        
        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTb = sizeGb * sizeKb;
        
        if (fileSize < sizeKb)
            return (df.format(fileSize/sizeKb) + " Kb");
        else if (fileSize < sizeMb)
            return (df.format(fileSize/sizeMb) + " Mb");
        else if (fileSize < sizeTb)
            return (df.format(fileSize/sizeGb) + " Gb");
        return "Greater than Tb";
    }
    
    private void printUsage() {
        System.out.println("Error: Arguments missing.");
        System.out.println("Usage: -ls [-d] [-h] [-R] <args> \n"
                + "Options: \n"
                + "-d: Directories are listed as plain files. \n"
                + "-h: Format file sizes in a human-readable fashion (eg. 54.0m"
                + " instead of 67108864). \n"
                + "-R: Recursively list subdirectories encountered. \n");
    }
}
