/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.adapters.builtin.functions;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class DateAndTime {
    private static final List<SimpleDateFormat> dateFormats = new ArrayList<>();
    static {
        dateFormats.add(new SimpleDateFormat("M/dd/yyyy"));
        dateFormats.add(new SimpleDateFormat("MM/dd/yyyy"));
        dateFormats.add(new SimpleDateFormat("dd.M.yyyy"));
        dateFormats.add(new SimpleDateFormat("dd.MM.yyyy"));
        dateFormats.add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
        dateFormats.add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
        dateFormats.add(new SimpleDateFormat("dd.MMM.yyyy"));
        dateFormats.add(new SimpleDateFormat("dd-MMM-yyyy"));
        dateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"));
    };    
    
    public static int yearOf(LocalDateTime date){
        return date.getYear();
    }
    
    public static int yearOf(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return Integer.parseInt(df.format(date));
    }

    public static int monthOf(LocalDateTime date){
        return date.getMonthValue();
    }

    public static int monthOf(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        return cal.get(Calendar.MONTH)+1;
    }

    public static int dayOfYearOf(LocalDateTime date){
        return date.getDayOfYear();
    }

    public static int dayOfYearOf(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public static java.lang.String monthNameOf(LocalDateTime date){
        return date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public static java.lang.String monthNameOf(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        DateFormatSymbols dfs = new DateFormatSymbols();
        return dfs.getMonths()[cal.get(Calendar.MONTH)];
    }

    public static int hourOf(LocalDateTime date){
        return date.getHour();
    }

    public static int hourOf(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int minuteOf(LocalDateTime date){
        return date.getMinute();
    }

    public static int minuteOf(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        return cal.get(Calendar.MINUTE);
    }

    public static int secondOf(LocalDateTime date){
        return date.getSecond();
    }

    public static int secondOf(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        return cal.get(Calendar.SECOND);
    }
}
