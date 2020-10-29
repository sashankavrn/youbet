package com.youbetcha.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    public static LocalDateTime convertToLocalDateTime(String timestamp) {
        //e.g 2020-08-05T00:00:00.0000000Z
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        return dateTime;
    }
}
