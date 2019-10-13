package jhu.searchindex;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Set;

public class SearchReducer extends Reducer<Text, CustomWriter, Text, CustomWriter> {
    
    @Override
    protected void setup(Context context) 
            throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<CustomWriter> values, 
            Context context) throws IOException, InterruptedException {
        
        //Instantiate CustomWriter object
        CustomWriter outputData = new CustomWriter();
        
        //Iterate over the values received from mapper
        for (CustomWriter filesAndIndices : values) {
            //Get the set of keys (filenames) from the CustomWriter
            Set<Text> fileNames = filesAndIndices.getKeys();
            //Iterate over the CustomWriter keys (filenames)
            for (Text fileName : fileNames) {
                //Add key-value pair (filename and offset list) to outputData
                outputData.addValues(fileName, filesAndIndices.get(fileName));
            }
        }       
        context.write(key, outputData);        
    }
}
