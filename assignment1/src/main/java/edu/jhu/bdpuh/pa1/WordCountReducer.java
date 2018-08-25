package pa1.bdpuh.jhu.edu;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer <Text, IntWritable, Text, IntWritable>{
	
    String mostFrequent = null;
    int topFrequency = 0;
	
	@Override
	protected void cleanup(
			Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		context.getCounter("Highest Frequency",mostFrequent).increment(topFrequency);
		super.cleanup(context);
	}

	@Override
	protected void setup(
			Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		super.setup(context);
		
	}

	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		Iterator<IntWritable> iter = values.iterator();
		
		while(iter.hasNext()) {
			sum += iter.next().get();
		}
		if (sum > topFrequency) {
			topFrequency = sum;
			mostFrequent = key.toString();
		}
		context.write(key, new IntWritable(sum));
	}

}
