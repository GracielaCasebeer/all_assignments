package jhu.searchindex;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;
import org.apache.hadoop.io.IntWritable;

public class SearchMapper extends Mapper<LongWritable, Text, Text, CustomWriter> {
    
    String fileName = null;
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
        
        //Get fileName
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
        
        //Intialize globalOffset
        int globalOffset = 0;
        
        //Initialize start of line offset
        int lineStartOffset = Integer.parseInt(key.toString());
        
        for (int i = 0; i < words.length; i++) {
            //Convert to lowercase
            words[i] = words[i].trim().toLowerCase();
            if (words[i].length() > 0 && !stopWords.contains(words[i])) {
                
                //Get partialLine up to the word being processed
                String[] partialLine = Arrays.copyOfRange(words, 0, i);
                
                //Initialize wordLineOffset
                int wordLineOffset = 0;
                
                //Calculate word offset in current line
                for (String w : partialLine) {
                    wordLineOffset = wordLineOffset + w.length();
                }
                
                //Calculate word offset in file
                globalOffset = lineStartOffset + wordLineOffset;
                
                CustomWriter dataMap = new CustomWriter();
                dataMap.add(new Text(fileName), new IntWritable(globalOffset));
                
                context.write(new Text(words[i]), dataMap);
            }
            context.getCounter("words", "count");
        }        
    }
}
