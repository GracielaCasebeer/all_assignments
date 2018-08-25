package edu.jhu.bdpuh;

import org.apache.commons.cli.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;


import java.io.*;

/**
 *
 * Usage: hadoop jar target/<jar-file> <main-class> -get -[<option>] <src> <localdst>
 *
 * Supports the following command and arguments get [-p] [-ignoreCrc] [-crc] <src> ... <localdst>
 *
 *
 */
public class GETCmd {

    private FileSystem hdfs;
    private String[] args;
    private LocalFileSystem localFileSystem;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();


    public GETCmd(FileSystem hdfs, String[] args, LocalFileSystem localFileSystem) {

        this.hdfs = hdfs;
        this.args = args;
        this.localFileSystem = localFileSystem;
        options.addOption("get", false, "Get files from hdfs and save them locally");
        options.addOption("ignoreCrc", false, "Skip CRC checks on the file(s) downloaded.");
        options.addOption("crc", false, "Write CRC checksums for the files downloaded..");
        options.addOption("p", false, "Preserves access and modification times, ownership and the permissions.");

    }

    private void getFile(Path input, String dest) throws IOException{


        File file = new File(dest);
        file.createNewFile();
        OutputStream os = new FileOutputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(hdfs.open(input)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        try {
            String line;
            line=br.readLine();
            while (line != null){
                bw.write(line+"\n");
                line = br.readLine();
            }
        } finally {
            br.close();
            os.close();
            bw.close();
        }

    }


    public void execute() throws IOException, ParseException {
        CommandLine line = parser.parse(options, args);
        System.out.println("get cmd detected parsing options..");
        if (line.hasOption("ignoreCrc")) System.out.println("has -ignoreCrc option");
        if (line.hasOption("crc")) System.out.println("has -crc option");
        if (line.hasOption("p")) System.out.println("has -p option");

        localFileSystem.setVerifyChecksum(!line.hasOption("ignoreCrc"));
        getFile(new Path(args[args.length-2]), args[args.length-1]);
        if(line.hasOption("p")){
            FileStatus fs = hdfs.getFileStatus(new Path(args[args.length-2]));
            Path localPath = new Path(args[args.length-1]);
            localFileSystem.setOwner(localPath, fs.getOwner(), fs.getGroup());
            localFileSystem.setTimes(localPath,fs.getModificationTime(),fs.getAccessTime());
        }

    }


}