package hdhw3;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.hadoop.io.IOUtils;

public class catCmnd {
    private FileSystem hdfs;
    private String[] args;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();
    
    public catCmnd(FileSystem hdfs, String[] args) {
        this.hdfs = hdfs;
        this.args = args;
        options.addOption("cat", false, "Copies source paths to stdout");
        options.addOption("ignoreCrc", false, "Disables checksum verification");
    }
    
    public void execute() {
        try {
            //Get options and arguments from command line
            CommandLine line = parser.parse(options, args);
            
            //Check if 
            if (line.getArgs().length == 0) {
                throw new MissingArgumentException("Argument missing.");
            }
            
            //Initialize boolean variables for -ignoreCrc option
            boolean ignoreCrc = false;
            
            //Get path
            Path path = new Path(args[args.length-1]);
            
            if (line.getOptions().length > 1 ) {
                //Print command and options requested
                System.out.print("-cat command received with option: ");
                Option[] cmdOptions = line.getOptions();
                for (Option opt : cmdOptions) {
                    System.out.print("-" + opt.getOpt() + " ");
                }
                //Update ignoreCrc if applicable
                if (line.hasOption("ignoreCrc")) ignoreCrc = true;
                
                displayFileContent(path, ignoreCrc);
                
            } else if (line.getOptions().length == 1){                
                System.out.println("-cat command received with no options");                
                displayFileContent(path, ignoreCrc);
                
            }
        } 
        catch (MissingArgumentException ex) {
            Logger.getLogger(catCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
            printUsage();
        }
        catch (ParseException ex) {
            Logger.getLogger(catCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
        }  
    }
    
    private void displayFileContent(Path path, boolean ignoreCrc) {
        //Set checksum verification if CRC cannot be ignored
        if (!ignoreCrc) {
            hdfs.setVerifyChecksum(!ignoreCrc);
        } else {
            hdfs.setVerifyChecksum(ignoreCrc);
        }
        
        System.out.println();
        
        try (InputStream in = hdfs.open(path)) {
            IOUtils.copyBytes(in, System.out, 4096, false);
        } catch (IOException ex) {
            Logger.getLogger(catCmnd.class.getName())
                    .log(Level.SEVERE, null, ex);
        }        
    }
    
    private void printUsage() {
        System.out.println("Error: Arguments missing.");
        System.out.println("Usage: -cat [-ignoreCrc] <src> \n"
                + "Options: \n"
                + "-ignoreCrc: Disables checksum verification. \n");
    }

}
