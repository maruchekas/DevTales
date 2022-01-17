package org.skillbox.devtales.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class TimeParser {

    public static LocalDateTime getLocalDateTime(long timestamp) {
        return (LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp),
                TimeZone.getDefault().toZoneId()));
    }

    public static long getTimestamp(LocalDateTime localDateTime){
        return ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    public static Date getDateFromLocalDateTime(LocalDateTime ldt){

        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
