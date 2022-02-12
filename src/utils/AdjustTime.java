
/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:

E.    Provide the ability to automatically adjust appointment times based on user time zones and daylight saving time.
    - Methods are provided to convert into UTC and user's local time. Daylight savings time is also automatically converted this way.

 */

package utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AdjustTime {

    public static LocalDateTime toUTC(LocalDateTime userTimeDT){
        ZonedDateTime userTimeZone = userTimeDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utcTime = userTimeZone.withZoneSameInstant(ZoneId.of("UTC"));
        return utcTime.toLocalDateTime();
    }

    //To my local date time.
    public static Timestamp toLocal(Timestamp timeSaved){
        LocalDateTime timeSavedDT = timeSaved.toLocalDateTime();
        ZonedDateTime timeSavedAtUTC =  timeSavedDT.atZone(ZoneId.of("UTC"));
        ZonedDateTime userZoneTime = timeSavedAtUTC.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        LocalDateTime userDT = userZoneTime.toLocalDateTime();
        return Timestamp.valueOf(userDT);
    }
}
