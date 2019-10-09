package hdhw3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.TrashPolicy;

class rmCmnd {
    private FileSystem hdfs;
    private String[] args;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();
    
    public rmCmnd(FileSystem hdfs, String[] args) {
        this.hdfs = hdfs;
        this.args = args;
        options.addOption("rm", false, "Delete files specified as arguments.");
        options.addOption("f", false, "Will not display a diagnostic message or"
                + " modify the exit status to reflect an error if the file does"
                + " not exist.");
        options.addOption("r", false, "Deletes the directory and any content "
                + "under it recursively.");
        options.addOption("R", false, "Deletes the directory and any content "
                + "under it recursively.");
        options.addOption("skipTrash", false, "Bypasses trash, if enabled, and "
                + "deletes the specified file(s) immediately.");
    }
    
    public void execute() {
        try {
            //Get options and arguments from command line
            CommandLine line = parser.parse(options, args);
            
            //Check if
            if (line.getArgs().length == 0) {
                throw new MissingArgumentException("Argument missing.");
            }
            
            //Initialize boolean variables for command options
            boolean forceRm = false;
            boolean recursiveRm = false;
            boolean skipTrashRm = false;
            
            //Get path
            Path path = new Path(args[args.length-1]);
            
            if (line.getOptions().length > 1) {
                //Print command and options requested
                System.out.print("-rm command received with option: ");
                Option[] cmdOptions = line.getOptions();
                for (Option opt : cmdOptions) {
                    System.out.print("-" + opt.getOpt() + " ");
                }
                
                //Update recursiveRm if applicable
                if (line.hasOption("r") || line.hasOption("R")) {
                    recursiveRm = true;
                }
                //Update forceRm if applicable
                if (line.hasOption("f")) forceRm = true;
                //Update skipTrashRm if applicable
                if (line.hasOption("skipTrash")) skipTrashRm = true;
                
                removeFile(path, recursiveRm, forceRm, skipTrashRm);
                
            } else if (line.getOptions().length == 1) {
                System.out.println("-rm command received with no options");
                
                removeFile(path, recursiveRm, forceRm, skipTrashRm);
            }
            
        } catch (MissingArgumentException ex) {
            Logger.getLogger(rmCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
            printUsage();
        } catch (ParseException ex) {
            Logger.getLogger(rmCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    private void removeFile(Path path, boolean recursiveRm, boolean forceRm, 
            boolean skipTrashRm) {
        try {
            if (!hdfs.exists(path) && !forceRm) {
                //Cannot remove a non-existing file
                System.out.println("\nrm: '" + path.toString() 
                        + "' :No such file or directory");
            } 
            else {                
                //Check if removal target is an empty directory
                if (hdfs.isDirectory(path) 
                        && hdfs.listStatus(path).length != 0 && !recursiveRm) {
                    
                    System.out.println ("rm: '" + path.toString() 
                            + "' :Is a non-empty directory");
                    
                } else {
                    //Remove target path
                    hdfs.delete(path, recursiveRm);                    
                    //Check if skipTrashRm option is enabled
                    if (skipTrashRm) {
                        System.out.println();                        
                        //Get TrashPolicy instance
                        TrashPolicy trashPolicy = TrashPolicy
                                .getInstance(hdfs.getConf(), hdfs, hdfs
                                        .getHomeDirectory());
                        //Check if trashPolicy is enabled
                        if (trashPolicy.isEnabled()) {
                            //Get trash path and erase it
                            Path trashDir = trashPolicy.getCurrentTrashDir();
                            hdfs.delete(trashDir, true);
                        }                        
                    }
                    
                    System.out.println("\nCommand finished executing.");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(rmCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    private void printUsage() {
        System.out.println("Error: Arguments missing.");
        System.out.println("Usage: -rm [-f] [-r|-R] [-skipTrash] <args> \n"
                + "Options: \n"
                + "-f: Will not display a diagnostic message or modify the exit"
                + " status to reflect an error if the file does not exist. \n"
                + "-r: Deletes the directory and any content under it "
                + "recursively. \n"
                + "-R: Equivalent to -r. \n"
                + "-skipTrash: If enabled, it will bypass and delete the "
                + "specified file(s) immediately. \n");
    }
}
