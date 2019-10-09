package hdhw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Shell.ExitCodeException;

public class getCmnd {
    private LocalFileSystem localfs;
    private FileSystem hdfs;
    private String[] args;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();
    
    public getCmnd(LocalFileSystem localfs, FileSystem hdfs, String[] args) {
        this.localfs = localfs;
        this.hdfs = hdfs;
        this.args = args;
        options.addOption("get", false, "");
        options.addOption("p", false, "");
        options.addOption("ignoreCrc", false, "");
        options.addOption("crc", false, "");
        options.addOption("f", false, "");
    }
    
    public void execute() {
        try {
            //Get options and arguments from command line
            CommandLine line = parser.parse(options, args);
                        
            //Check if there are missing arguments
            if (line.getArgs().length != 2) {
                throw new MissingArgumentException("Argument missing.");
            }
            
            //Initialize boolean variables get options
            boolean preserve = false;
            boolean ignoreCrc = false;
            boolean crc = false;
            boolean overwrite = false;
            
            //Get paths
            Path sourcePath = new Path(args[args.length-2]);
            String destination = args[args.length-1];
            
            if (line.getOptions().length > 1 ) {
                //Print command and options requested
                System.out.print("-get command received with option: ");
                Option[] cmdOptions = line.getOptions();
                for (Option opt : cmdOptions) {
                    System.out.print("-" + opt.getOpt() + " ");
                }
                //Update preserve if applicable
                if (line.hasOption("p")) preserve = true;
                //Update ignoreCrc if applicable
                if (line.hasOption("ignoreCrc")) ignoreCrc = true;
                //Update crc if applicable
                if (line.hasOption("crc")) crc = true;
                //Update overwrite if applicable
                if (line.hasOption("f")) overwrite = true;
                                
                copyFile(sourcePath, destination, ignoreCrc, crc, preserve, 
                        overwrite);
                
            } else if (line.getOptions().length == 1){                
                System.out.println("-get command received with no options");                
                
                copyFile(sourcePath, destination, ignoreCrc, crc, preserve, 
                        overwrite);
            }            
        } catch (MissingArgumentException ex) {
            Logger.getLogger(getCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
            printUsage();
        } catch (ParseException ex) {
            Logger.getLogger(getCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    private void copyFile(Path source, String destination, boolean ignoreCrc, 
            boolean crc, boolean preserve, boolean overwrite) {
        //Set checksum verification if CRC cannot be ignored
        if (!ignoreCrc) {
            localfs.setVerifyChecksum(!ignoreCrc);
        }
        else {
            localfs.setVerifyChecksum(ignoreCrc);
        }
        //Set write checksum flag
        localfs.setWriteChecksum(crc);
        
        //Instantiate file object
        File file = new File(destination);
        
        //Check file presence and overwrite option
        
        if (file.exists() && !overwrite) {
            //Cannot overwrite existing file
            System.out.println("get: '" + destination + "' :File exists");
            
        } else {
            //Create new, empty file
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(getCmnd.class.getName())
                        .log(Level.SEVERE, null, ex);
            }

            //Copy content to new file
            try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(hdfs.open(source)));
                    BufferedWriter bw = new BufferedWriter(
                            new FileWriter(file));){            
                String line;
                line = br.readLine();
                while (line != null) {
                    bw.write(line);
                    bw.newLine();
                    line = br.readLine();
                }                                

            } catch (IOException ex) {
                Logger.getLogger(getCmnd.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            
            if (preserve) {
                updatePermissions(source, destination);
            }            
            
            System.out.println("\nCommand finished executing.");
        }
    }    

    private void updatePermissions(Path source, String destination) {
        FileStatus fs = null;
        Path localPath = null;
        try {
            //Update modification times, ownership and permissions if needed
            fs = hdfs.getFileStatus(source);
            localPath = new Path(destination);
            
            localfs.setTimes(localPath, fs.getModificationTime(), 
                        fs.getAccessTime());                    
            localfs.setPermission(localPath, fs.getPermission());
            localfs.setOwner(localPath, fs.getOwner(), fs.getGroup());
            
        } catch (ExitCodeException ex) {
            //hdfs file system group does not exist in local file system
            //hence, defaulting group to file owner    
            System.out.println("Source file system group does not exist in "
                    + "destination file system. \nHence, cannot change "
                    + "destination file ownership due to the following "
                    + "error:");
            System.out.println(ex);
            
        } catch (IOException ex) {
            Logger.getLogger(getCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        System.out.println("\nPermissions that were able to propagate accross "
                + "file systems were preserved.");
    }
    
    private void printUsage() {
        System.out.println("Error: Arguments missing.");
        System.out.println("Usage: -get [-p] [-ignoreCrc] [-crc] [-f] "
                + "<src> <localdst> \n"
                + "Options: \n"
                + "-p: Preserves access and modification times, ownership and "
                + "the permissions. \n"
                + "-ignoreCrc: Disables checksum verification. \n"
                + "-crc: Enables checksum verification. \n"
                + "-f: Overwrites the destination if it already exists.\n");
    }
}
