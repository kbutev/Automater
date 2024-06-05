/*
 * Created by Kristiyan Butev.
 * Copyright © 2019 Kristiyan Butev. All rights reserved.
 */
package automater.utilities;

import org.jetbrains.annotations.NotNull;
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

    public static @NotNull String asStandartDate(@NotNull Date date) {
        return StandartFormatter.format(date);
    }

    public static @NotNull String asTimestamp(@NotNull Date date) {
        return TimestampFormatter.format(date);
    }

    public static @NotNull String asYearMonthDay(@NotNull Date date) {
        return YYYMMDDFormatter.format(date);
    }

    public static @NotNull String asMonthDay(@NotNull Date date) {
        return MMDDFormatter.format(date);
    }

    public static @NotNull String asHourMinuteSecond(@NotNull Date date) {
        return HMSFormatter.format(date);
    }

    public static @NotNull String asHourMinuteSecondMilisecond(@NotNull Date date) {
        return HMSMMFormatter.format(date);
    }

    public static @NotNull String asHourMinute(@NotNull Date date) {
        return HMFormatter.format(date);
    }
}
