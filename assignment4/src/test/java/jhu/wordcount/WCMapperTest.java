package jhu.wordcount;

import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by wilsopw1 on 2/26/17.
 */
public class WCMapperTest {
    MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;

    @Before
    public void setUp() throws Exception {
        WCMapper mapper = new WCMapper();
        mapDriver = MapDriver.newMapDriver(mapper);
    }

    @Test
    public void testMapper() throws IOException {
        mapDriver.withInput(new LongWritable(), new Text("hello world"));
        mapDriver.withOutput(new Text("hello"), new IntWritable(1));
        mapDriver.withOutput(new Text("world"), new IntWritable(1));
        mapDriver.runTest();
    }

}