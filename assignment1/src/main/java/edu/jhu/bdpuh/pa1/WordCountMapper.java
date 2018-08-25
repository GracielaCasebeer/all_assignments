package pa1.bdpuh.jhu.edu;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<Object,Text,Text,IntWritable>{
	
	static final List<String> StopWords = Arrays.asList("a", "the", "that", "this", "then", "those");
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		context.getCounter("lines", "count").increment(1);
		String words[] = value.toString().replaceAll("[^a-zA-Z ]", "").split("\\s+");
		context.getCounter("words", "count").increment(words.length);
		for (String w : words) {
			w = w.trim().toLowerCase();
			if (w.length() > 0 && !StopWords.contains(w))
				context.write(new Text(w), new IntWritable(1));
		}
	}
}
