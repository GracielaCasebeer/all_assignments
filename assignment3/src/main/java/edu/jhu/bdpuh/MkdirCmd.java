package edu.jhu.bdpuh;

import org.apache.commons.cli.*;

public class MkdirCmd {
    Options options = new Options();
    CommandLineParser parser = new BasicParser();

    {
        options.addOption("mkdir", false, "make directory");
        options.addOption("p",false, "create parent");
    }
    public MkdirCmd(String args[]) {
        try {
            CommandLine line = parser.parse(options, args);
            if(line.hasOption("p"))
                System.out.println("has -p option");
            String[] cmdargs = line.getArgs();
            for (String a : cmdargs) {
                System.out.println(a);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
