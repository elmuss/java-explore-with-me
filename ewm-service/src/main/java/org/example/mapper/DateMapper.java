package org.example.mapper;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateMapper {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Instant instantFromString(String stringTime) {
        LocalDateTime ldt = LocalDateTime.parse(stringTime, dtf);
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.of("UTC+3"));

        return Instant.from(zdt);
    }

    public static String stringFromInstant(Instant instantTime) {
        ZonedDateTime zdt = instantTime.atZone(ZoneId.of("UTC+3"));

        return zdt.format(dtf);
    }
}
