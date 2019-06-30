/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Bytevi
 */
public class Logger {
    public static final String MESSAGE_ACTION_PREFIX = "#ACTION: ";
    public static final String MESSAGE_EVENT_PREFIX = "#EVENT: ";
    public static final String WARNING_PREFIX = "#WARNING: ";
    public static final String ERROR_PREFIX = "#ERROR: ";
    
    public static void message(String string)
    {
        System.out.println(string);
    }
    
    public static void messageAction(String string)
    {
        System.out.println(generateTimestamp(MESSAGE_ACTION_PREFIX) + string);
    }
    
    public static void messageEvent(String string)
    {
        System.out.println(generateTimestamp(MESSAGE_EVENT_PREFIX) + string);
    }
    
    public static void warning(String string)
    {
        System.out.println(generateTimestamp(WARNING_PREFIX) + string);
    }
    
    public static void error(String string)
    {
        System.out.println(generateTimestamp(ERROR_PREFIX) + string);
    }
    
    private static String generateTimestamp(String prefix)
    {
        return prefix.length() > 0 ? getTimestamp() + " " + prefix : "";
    }
    
    private static String getTimestamp()
    {
        String pattern = "HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        
        return simpleDateFormat.format(new Date());
    }
}
