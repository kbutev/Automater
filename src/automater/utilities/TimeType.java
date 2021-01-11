/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2021 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public enum TimeType {
    milliseconds, seconds, minutes, hours, days;
    
    // Returns list of ms, sec, minutes and hours.
    static public List<TimeType> listOfSimpleTypes() {
        TimeType[] result = values();
        
        ArrayList l = new ArrayList(Arrays.asList(result));
        l.remove(TimeType.days);
        
        return l;
    }
    
    public String stringValue() {
        switch (this) {
            case milliseconds:
                return "ms";
            case seconds:
                return "sec";
            case minutes:
                return "min";
            case hours:
                return "hours";
            case days:
                return "days";
            default:
                return "sec";
        }
    }
    
    // Converts the given time value, using the instance time type, to ms.
    public long asMilliseconds(String value) {
        double time;
        
        switch (this) {
            case milliseconds:
                return Long.parseLong(value);
            case seconds:
                time = Double.parseDouble(value);
                time *= 1000.0;
                return (long)time;
            case minutes:
                time = Double.parseDouble(value);
                time *= 1000.0;
                time *= 60.0;
                return (long)time;
            case hours:
                time = Double.parseDouble(value);
                time *= 1000.0;
                time *= 60.0;
                time *= 60.0;
                return (long)time;
            case days:
                time = Double.parseDouble(value);
                time *= 1000.0;
                time *= 60.0;
                time *= 60.0;
                time *= 24.0;
                return (long)time;
            default:
                throw new RuntimeException("Bad parameter");
        }
    }
    
    // Converts the given time value, using the instance time type, to sec.
    public double asSeconds(String value) {
        double ms = (double)asMilliseconds(value);
        return ms / 1000.0;
    }
    
    public String asStringFromMS(long ms) {
        double time;
        
        switch (this) {
            case milliseconds:
                return String.format("%d", ms);
            default:
                time = convertMSToValue(ms, this);
                return String.format("%.3f", time);
        }
    }
    
    static public TimeType fromStringValue(String value) {
        switch (value) {
            case "ms":
                return TimeType.milliseconds;
            case "sec":
                return TimeType.seconds;
            case "min":
                return TimeType.minutes;
            case "hours":
                return TimeType.hours;
            case "days":
                return TimeType.days;
            default:
                return TimeType.valueOf(value);
        }
    }
    
    // Returns the most appropriate type to display the given time value.
    // For example, if the time value is over a day, the return type will be days.
    static public TimeType appropriateTypeForMS(long ms) {
        double inSeconds = ms;
        inSeconds /= 1000.0;
        
        long minutesMinValue = 600;
        long hoursMinValue = 3600;
        long dayMinValue = 86400;
        
        if (inSeconds > dayMinValue) {
            return TimeType.days;
        }
        
        if (inSeconds > hoursMinValue) {
            return TimeType.hours;
        }
        
        if (inSeconds > minutesMinValue) {
            return TimeType.minutes;
        }
        
        if (inSeconds >= 1.0) {
            return TimeType.seconds;
        }
        
        return TimeType.milliseconds;
    }
    
    static public double convertMSToValue(long ms, @NotNull TimeType destType) {
        double time;
        
        switch (destType) {
            case milliseconds:
                return (double)ms;
            case seconds:
                time = (double)ms;
                time /= 1000.0;
                return time;
            case minutes:
                time = (double)ms;
                time /= 1000.0;
                time /= 60.0;
                return time;
            case hours:
                time = (double)ms;
                time /= 1000.0;
                time /= 60.0;
                time /= 60.0;
                return time;
            case days:
                time = (double)ms;
                time /= 1000.0;
                time /= 60.0;
                time /= 60.0;
                time /= 24.0;
                return time;
            default:
                throw new RuntimeException("Bad parameter");
        }
    }
}
