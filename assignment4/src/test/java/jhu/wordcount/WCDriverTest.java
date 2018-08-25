package jhu.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by wilsopw1 on 2/26/17.
 */
public class WCDriverTest {
    MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

    @Before
    public void setUp() throws Exception {
        WCMapper mapper = new WCMapper();
        WCReducer reducer = new WCReducer();
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);

    }

    @Test
    public void run() throws Exception {
        mapReduceDriver.withInput(new LongWritable(), new Text("hello world hello world"));
        mapReduceDriver.withInput(new LongWritable(), new Text("hadoop world hadoop world"));
        mapReduceDriver.withOutput(new Text("hadoop"), new IntWritable(2));
        mapReduceDriver.withOutput(new Text("hello"), new IntWritable(2));
        mapReduceDriver.withOutput(new Text("world"), new IntWritable(4));
        mapReduceDriver.runTest();
    }

}