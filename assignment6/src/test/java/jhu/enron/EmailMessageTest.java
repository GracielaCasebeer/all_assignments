package jhu.enron;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by hduser on 3/8/17.
 */
public class EmailMessageTest {
    Gson gson = new GsonBuilder().create();
    Map<String, String> expectedValues = new HashMap<String, String>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z (z)");

    @Before
    public void setup() {
        expectedValues.put("From", "fran.fagan@enron.com");
        expectedValues.put("To", "lynn.blair@enron.com");
        expectedValues.put("Subject", "FW: Promotions and Transfers- Gas Logistics");
    }

    @Test
    public void jsonMappingTest() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource("email-test.json").getFile());
        Gson gson = new GsonBuilder().create();
        EmailMessage msg = gson.fromJson(new FileReader(testFile),
                EmailMessage.class);
        assertEquals("/home/hduser/data/enron/maildir/blair-l/personnel___promotions/1.",
                     msg.file);

        String date = msg.header.get("Date");
        ZonedDateTime dateTime = ZonedDateTime.parse(date, formatter);
        long epoch = dateTime.toEpochSecond();

        System.out.printf("Time: %s => %s => %d\n", date, dateTime, epoch   );
        for(String v : expectedValues.keySet())
            assertEquals(expectedValues.get(v),msg.header.get(v));
    }

}