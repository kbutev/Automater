/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Collection of utility methods for converting dates to strings.
 * 
 * @author Bytevi
 */
public class DateUtilities {
    public static final SimpleDateFormat StandartFormatter = new SimpleDateFormat("YYY.MM.dd HH:mm");
    public static final SimpleDateFormat TimestampFormatter = new SimpleDateFormat("YYY.MM.dd HH:mm:ss.SSS");
    public static final SimpleDateFormat YYYMMDDFormatter = new SimpleDateFormat("YYY.MM.dd");
    public static final SimpleDateFormat MMDDFormatter = new SimpleDateFormat("MM.dd");
    public static final SimpleDateFormat HMSFormatter = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat HMSMMFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
    public static final SimpleDateFormat HMFormatter = new SimpleDateFormat("HH:mm");
    
    public static String asStandartDate(Date date)
    {
        return StandartFormatter.format(date);
    }
    
    public static String asTimestamp(Date date)
    {
        return TimestampFormatter.format(date);
    }
    
    public static String asYearMonthDay(Date date)
    {
        return YYYMMDDFormatter.format(date);
    }
    
    public static String asMonthDay(Date date)
    {
        return MMDDFormatter.format(date);
    }
    
    public static String asHourMinuteSecond(Date date)
    {
        return HMSFormatter.format(date);
    }
    
    public static String asHourMinuteSecondMilisecond(Date date)
    {
        return HMSMMFormatter.format(date);
    }
    
    public static String asHourMinute(Date date)
    {
        return HMFormatter.format(date);
    }
}
