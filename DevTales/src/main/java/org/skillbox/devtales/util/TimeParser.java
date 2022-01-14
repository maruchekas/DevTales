package org.skillbox.devtales.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeParser {

    public static LocalDateTime getLocalDateTime(long dateWithTimestampAccessor) {
        return LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(dateWithTimestampAccessor)),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
