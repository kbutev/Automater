/*
 *  Created by Kristiyan Butev.
 *  Copyright © 2021 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public enum TimeType {
    milliseconds, seconds, minutes, hours, days;

    // Basic construction method.
    // Use this over the built-in valueOf() method.
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

    // Returns list of ms, sec, minutes and hours.
    static public List<TimeType> listOfSimpleTypes() {
        ArrayList l = new ArrayList();
        l.add(TimeType.milliseconds);
        l.add(TimeType.seconds);
        l.add(TimeType.minutes);
        l.add(TimeType.hours);
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

    // If the given string cannot be parsed to a number, zero is returned.
    public @NotNull TimeValue asTimeFromString(@NotNull String value) {
        double d;

        try {
            d = Double.parseDouble(value);
        } catch (Exception e) {
            return TimeValue.zero();
        }

        switch (this) {
            case milliseconds:
                return TimeValue.fromMilliseconds((long) d);
            case seconds:
                return TimeValue.fromSeconds(d);
            case minutes:
                return TimeValue.fromMinutes(d);
            case hours:
                return TimeValue.fromHours(d);
            case days:
                return TimeValue.fromDays(d);
            default:
                return TimeValue.zero();
        }
    }

    public @NotNull String asStringFromTime(@NotNull TimeValue time) {
        switch (this) {
            case milliseconds:
                return time.inMillisecondsAsString();
            case seconds:
                return String.format("%.2f", time.inSeconds());
            case minutes:
                return String.format("%.2f", time.inMinutes());
            case hours:
                return String.format("%.3f", time.inHours());
            case days:
                return String.format("%.3f", time.inDays());
            default:
                return time.toString(false);
        }
    }
}
