/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automater.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logs various messages to the standart output.
 * 
 * @author Bytevi
 */
public class Logger {
    public static final boolean DISPLAY_TIMESTAMPS = true;
    
    public static final String MESSAGE_ACTION_PREFIX = "#ACTION#";
    public static final String MESSAGE_EVENT_PREFIX = "#EVENT#";
    public static final String WARNING_PREFIX = "#WARNING#";
    public static final String ERROR_PREFIX = "#ERROR#";
    public static final String SYSTEM_ERROR_PREFIX = "#SYSTEM_ERROR#";
    
    public static final String OVERRIDE_ME_MESSAGE = "Calling non-overriden base method ";
    
    public static <T> void message(T origin, String string)
    {
        System.out.println(generateText(origin, "", string));
    }
    
    public static <T> void messageAction(T origin, String string)
    {
        System.out.println(generateText(origin, MESSAGE_ACTION_PREFIX, string));
    }
    
    public static <T> void messageEvent(T origin, String string)
    {
        System.out.println(generateText(origin, MESSAGE_EVENT_PREFIX, string));
    }
    
    public static <T> void warning(T origin, String string)
    {
        System.out.println(generateText(origin, WARNING_PREFIX, string));
    }
    
    public static <T> void error(T origin, String string)
    {
        System.out.println(generateText(origin, ERROR_PREFIX, string));
    }
    
    public static <T> void systemError(T origin, String string)
    {
        System.out.println(generateText(origin, SYSTEM_ERROR_PREFIX, string));
    }
    
    public static <T> void overrideMe(T origin, String methodName)
    {
        System.out.println(generateText(origin, WARNING_PREFIX, OVERRIDE_ME_MESSAGE + methodName));
    }
    
    private static <T> String generateText(T origin, String prefix, String text)
    {
        String reportingClass = origin.getClass().getSimpleName();
        String textPrefix = generatePrefix(prefix);
        
        return textPrefix + " " + reportingClass + ": " + text;
    }
    
    private static String generatePrefix(String prefix)
    {
        if (!DISPLAY_TIMESTAMPS)
        {
            return prefix;
        }
        
        return prefix.length() > 0 ? getTimestamp() + " " + prefix : getTimestamp();
    }
    
    private static String getTimestamp()
    {
        String pattern = "HH:mm:ss.SSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        
        return simpleDateFormat.format(new Date());
    }
}
