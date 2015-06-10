package xqt.adapters.builtin.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */

// maybe it is better to put these function pack classes into a standalone jar.
public class String {
    private static final List<SimpleDateFormat> dateFormats = new ArrayList<>();
    private static final List<SimpleDateFormat> timeFormats = new ArrayList<>();
    private static final List<SimpleDateFormat> dateTimeFormats = new ArrayList<>();
    static {
        dateFormats.add(new SimpleDateFormat("M/dd/yyyy"));
        dateFormats.add(new SimpleDateFormat("MM/dd/yyyy"));
        dateFormats.add(new SimpleDateFormat("dd.M.yyyy"));
        dateFormats.add(new SimpleDateFormat("dd.MM.yyyy"));
        dateFormats.add(new SimpleDateFormat("dd.MMM.yyyy"));
        dateFormats.add(new SimpleDateFormat("dd-MMM-yyyy"));
        dateFormats.add(new SimpleDateFormat("yyyy-MMM-dd"));
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd"));
        
        timeFormats.add(new SimpleDateFormat("hh:mm:ss a"));
        timeFormats.add(new SimpleDateFormat("hh:mm:ss"));
        timeFormats.add(new SimpleDateFormat("hh:mm"));

        dateTimeFormats.add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
        dateTimeFormats.add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
        dateTimeFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"));
    };    
            
    public static int indexOf(java.lang.String s1, java.lang.String s2){
        if(s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty())
            return -1;
        return s1.indexOf(s2);
    }

    public static boolean isDate(java.lang.String s){ // it is a heavy method
        Date date = null;
        if(s == null) {
            return false;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
                format.setLenient(false);
                date = format.parse(s);
            } catch (ParseException e) {
                //try other formats
            }
            if (date != null) {
                break;
            }
        }
        return date != null;
    }
    
    public static boolean isEmpty(java.lang.String s){
        if(s == null)
            return true;
        return s.isEmpty();
    }

    public static boolean isNumeric(java.lang.String s) {
       if (s == null) {
           return false;
       }
       return s.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
//       int sz = s.length();
//       for (int i = 0; i < sz; i++) {
//           if (Character.isDigit(s.charAt(i)) == false) {
//               return false;
//           }
//       }
//       return true;
    }

    public static int lastIndexOf(java.lang.String s1, java.lang.String s2){
        if(s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty())
            return -1;
        return s1.lastIndexOf(s2);
    }

    public static int length(java.lang.String s){
        if(s == null)
            return 0;
        return s.length();
    }

    public static java.lang.String lower(java.lang.String s){
        if(s == null)
            return null;
        return s.toLowerCase();
    }

//    public static java.lang.String ltrim(java.lang.String s) {
//        if (s == null) {
//            return null;
//        }
//        int i = 0;
//        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
//            i++;
//        }
//        return s.substring(i);
//    }    
    
    public static java.lang.String ltrim(java.lang.String s, java.lang.String pattern) {
        if (s == null || pattern == null || pattern.length() <=0) {
            return s;
        }
        java.lang.String normalizedText = s.trim();
        int index = 0;
        char character = pattern.charAt(0);
        while (normalizedText.charAt(index) == character) {
          index++;
        }
        return normalizedText.substring(index).trim();
    }
    
    public static java.lang.String replace(java.lang.String source, java.lang.String target, java.lang.String replacement) {
        if(source == null)
            return null;
        return source.replace(target, replacement);
    }    
    
    public static java.lang.String reverse(java.lang.String s) {
        if (s == null) {
            return null;
        }
        return new StringBuilder(s).reverse().toString();
    }

//    public static java.lang.String rtrim(java.lang.String s) {
//        if (s == null) {
//            return null;
//        }
//        int i = s.length()-1;
//        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
//            i--;
//        }
//        return s.substring(0,i+1);
//    }

    public static java.lang.String rtrim(java.lang.String s, java.lang.String pattern) {
        if (s == null || pattern == null || pattern.length() <=0) {
            return s;
        }
        java.lang.String normalizedText = s.trim();
        char character = pattern.charAt(0);
        int index = normalizedText.length() - 1;

        while (normalizedText.charAt(index) == character) {
          if (--index < 0) {
            return "";
          }
        }
        return normalizedText.substring(0, index + 1).trim();
      }

    public static java.lang.String substring(java.lang.String s, int beginIndex, int endIndex) {
        if (s == null) {
            return null;
        }
        return s.substring(beginIndex, endIndex);
    }

//    public static java.lang.String trim(java.lang.String s) {
//        if (s == null) {
//            return null;
//        }
//        return s.trim();
//    }    

    public static java.lang.String trim(java.lang.String s, java.lang.String pattern) {
        if (s == null) {
            return null;
        }
        return ltrim(rtrim(s, pattern), pattern);
    }    
    
    public static java.lang.String upper(java.lang.String string){
        if(string == null)
            return null;
        return string.toUpperCase();
    }
    
     public static Integer toInteger(java.lang.String s){
         if(isNumeric(s)){
             return Integer.parseInt(s);
         }
         return null;
     }

     public static Long toLong(java.lang.String s){
         if(isNumeric(s)){
             return Long.parseLong(s);
         }
         return null;
     }

     public static Double toDouble(java.lang.String s){
         if(isNumeric(s)){
             return Double.parseDouble(s);
         }
         return null;
     }

     public static Boolean toBoolean(java.lang.String s){
            return Boolean.parseBoolean(s);
     }

    public static Date toDate(java.lang.String s, java.lang.String format){
//        if(isDate(s)){ 
            try {
            // do not use isDate, it parses/ creates the date object
            // does not guarantees that the format is one of the pre-defined formatts
            //return LocalDate.parse(s, DateTimeFormatter.ofPattern(format));
                return (new SimpleDateFormat(format)).parse(s);
            } catch (ParseException ex) {
                return null;
            }
//        }
//        return null;
    }
     
}
