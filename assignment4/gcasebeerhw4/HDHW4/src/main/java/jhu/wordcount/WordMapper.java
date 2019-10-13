package jhu.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    String inputPath = null;
    String stops = "src/main/resources/stops.txt";
    List<String> stopWords = new ArrayList<>();

    @Override
    protected void setup(Context context) throws IOException, 
            InterruptedException {
        
        super.setup(context);
        // Read configuration information
        this.inputPath = context.getConfiguration().get("inputPath");
        
        //Initialize list of stop words
        try (Stream<String> stream = Files.lines(Paths.get(stops))) {
            stopWords = stream.collect(Collectors.toList());
            for (int i = 0; i < stopWords.size(); i++ ) {
                stopWords.set(i, stopWords.get(i).trim().replaceAll("'", ""));
            }            
        } catch (IOException ex) {
            System.out.println(ex);
        }        
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {
        
        String fileName = null;
        
        if(key.get() == 0) {
            //Get input filename
            fileName = ((FileSplit) context.getInputSplit())
                    .getPath()
                    .getName();
            context.getCounter("file", fileName).increment(1);
        }

        //Clean line characters and split words in array        
        String[] words = value.toString().replaceAll("[^a-zA-Z ]", "")
                .split("\\s+");
        context.getCounter("lines", "count");
        
        for(String w : words) {
            //Convert to lowercase
            w = w.trim().toLowerCase();
            if (w.length() > 0 && !stopWords.contains(w)) {
                context.write(new Text(w), new IntWritable(1));
            }
            context.getCounter("words", "count");
        }            
    }
}
