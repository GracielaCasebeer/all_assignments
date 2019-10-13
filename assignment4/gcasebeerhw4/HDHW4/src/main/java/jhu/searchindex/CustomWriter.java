/*
I used the following code as a sample to build my CustomWriter:
https://github.com/deleteman/inverted-index-java/blob/master/src/main/java/com/globant/training/invertedIndex/WordIntArrayDict.java
*/

package jhu.searchindex;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class CustomWriter implements Writable {
    
    private HashMap<Text, HashSet<IntWritable>> data;
    
    //Constructor
    public CustomWriter () {
        this.data = new HashMap<Text, HashSet<IntWritable>>();
    }
    
    //Add key (file name) and value (word offset in file)
    public void add(Text key, IntWritable value) {
        //Get HashSet for current key (filename)
        HashSet<IntWritable> offsetList = data.get(key);
        
        //Check if current key (filename) is new.
        if (offsetList == null) {
            //New key. Create corresponding new HashSet value to store offsets.
            offsetList = new HashSet<IntWritable>();
        } else {
            //Not a new key. Get HashSet value for current key.
            offsetList = data.get(key);
        }        
        //Add new offset to HashSet
        offsetList.add(value);
        //Update key-value pair in data
        data.put(key, offsetList);
    }
    
    //Add key (file name) and value (list of word offsets in file)
    public void addValues(Text key, HashSet<IntWritable> value) {
        //Get HashSet for current key (filename)
        HashSet<IntWritable> offsetList = data.get(key);
        
        //Check if current key (filename) is new
        if (offsetList == null) {
            //New key. Create corresponding new HashSet value to store offsets.
            offsetList = new HashSet<IntWritable>();
        }
        //Add new HashSet value to existing offsetList.
        offsetList.addAll(value);
        //Update key-value pair in data
        data.put(key, offsetList);
        /*
        boolean nullList = false;
        if (tempList == null) {
            tempList = new HashSet<IntWritable>();
            nullList = true;
        }
        tempList.addAll(value);
        if (nullList) {
            data.put(key, tempList);
        }
        */
    }
    
    public Set<Text> getKeys() {
        //Get keys in data HashMap
        return data.keySet();
    }
    
    public HashSet<IntWritable> get(Text key) {
        //Get indices HashSet for specified key
        return data.get(key);
    }
    
    public String toString() {
        StringBuilder line = new StringBuilder();
        for (Map.Entry<Text, HashSet<IntWritable>> item : data.entrySet()) {
            //Append filename
            line.append("@").append(item.getKey().toString());
            //Append separator
            line.append("|");
            for (IntWritable offset : item.getValue()) {
                line.append(offset.toString()).append("|");
            }
        }
        return line.toString();        
    }

    @Override
    public void write(DataOutput d) throws IOException {
        //Write to output stream number of keys map
        d.writeInt(data.keySet().size());
        //Iterate through map
        for (Map.Entry<Text, HashSet<IntWritable>> item : data.entrySet()) {
            //Write key to output stream
            item.getKey().write(d);
            //Write to output stream number of elements in hashset
            d.writeInt(item.getValue().size());
            //Iterate through hashset
            for (IntWritable i : item.getValue()) {
                //Write values to output stream
                i.write(d);
            }
        }
    }

    @Override
    public void readFields(DataInput di) throws IOException {
        //Make sure the data map is clean
        data.clear();
        //Set number of keys to data read from input stream
        int numberOfKeys = di.readInt();
        //Iterate through map
        for (int i = 0; i < numberOfKeys; i++) {
            //Intantiate key instance
            Text key = new Text();
            //Set key to data read from input stream
            key.readFields(di);
            //Instantiate Hashset to receive indices
            HashSet<IntWritable> offsetList = new HashSet<IntWritable>();
            //Set number of values to data read from input stream
            int numberOfValues = di.readInt();
            for (int j = 0; j < numberOfValues; j++) {
                //Instantiate offset instance
                IntWritable value = new IntWritable();
                //Set value to data read from input stream
                value.readFields(di);
                //Add offset value read from input stream to the HashSet
                offsetList.add(value);
            }
            //Add key-value pair to the data HashMap
            data.put(key, offsetList);
        }
    }    
}
