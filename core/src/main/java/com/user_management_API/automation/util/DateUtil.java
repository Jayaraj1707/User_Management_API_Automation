package com.user_management_API.automation.util;

import org.joda.time.*;
import org.joda.time.format.*;

import java.util.*;

public class DateUtil {

    /** The Constant LARGE_FORMAT. */
    public static final String LARGE_FORMAT = "EEEE, MMMM dd, yyyy HH:mm:ss aa";

    /** The Constant HOUR_FORMAT. */
    public static final String HOUR_FORMAT = "HH:mm:ss aa";

    /** The Constant MM_DD_YY. */
    public static final String MM_DD_YY = "MM/dd/yy HH:mm aa";


    public static String formatToZone(Long milliseconds, DateTimeZone zone, String pattern) {

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        fmt = fmt.withZone(zone);
        String strDate = fmt.print(new DateTime(milliseconds));
        return strDate;
    }
}
