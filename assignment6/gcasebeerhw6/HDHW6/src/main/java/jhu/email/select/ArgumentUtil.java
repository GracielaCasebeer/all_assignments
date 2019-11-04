/*
 * Source:
 * https://stackoverflow.com/questions/2704857/how-to-check-if-a-given-regex-is-valid
 */
package jhu.email.select;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ArgumentUtil {
    
    public static boolean isValidRegex(String pattern) {
        try {
            Pattern.compile(pattern);
        } catch (PatternSyntaxException exception) {
            return false;
        }
        return true;
    }
    
    public static boolean isValidDate(String date) {
        //Define date formatters
        DateTimeFormatter form1 = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter form2 = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        //Check date formatting
        try {
            //Check format YYYY-MM-DD
            LocalDate.parse(date, form1);
        } catch (DateTimeParseException e1) {
            try {
                //Check format YYYY-MM-DDTHH:MM:SSZ
                LocalDate.parse(date, form2);
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
        return true;
    }
    
    public static LocalDate getDate(String date) {
        //Declare formattedDate variable
        LocalDate formattedDate;
        //Define date formatters
        DateTimeFormatter form1 = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter form2 = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        try {
            //Check format YYYY-MM-DD
            formattedDate = LocalDate.parse(date, form1);
        } catch (DateTimeParseException e1) {
            try {
                //Check format YYYY-MM-DDTHH:MM:SSZ
                formattedDate = LocalDate.parse(date, form2);
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
        return formattedDate;
    }
    
    public static LocalDate getDate(Long date) {
        Instant instant = Instant.ofEpochSecond(date);
        LocalDateTime localDateTime = 
                LocalDateTime.ofInstant(instant, ZoneId.of("Z"));
        return localDateTime.toLocalDate();
    }
    
    public static LocalDateTime getDateTime(Long date) {
        Instant instant = Instant.ofEpochSecond(date);
        return LocalDateTime.ofInstant(instant, ZoneId.of("Z"));
    }
}
