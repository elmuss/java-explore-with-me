package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.StatsHitDto;
import ru.practicum.explorewithme.model.Stats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsHitMapper {
    public static StatsHitDto modelToDto(Stats hitEndpoint) {
        return StatsHitDto.builder()
                .id(hitEndpoint.getId())
                .app(hitEndpoint.getApp())
                .uri(hitEndpoint.getUri())
                .ip(hitEndpoint.getIp())
                .timestamp(String.valueOf(hitEndpoint.getTimestamp()))
                .build();
    }

    public static Stats modelFromDto(StatsHitDto newHit) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime ldt = LocalDateTime.parse(newHit.getTimestamp(), dtf);
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.of("UTC+0"));
        Instant instant = Instant.from(zdt);

        return Stats.builder()
                .id(newHit.getId())
                .app(newHit.getApp())
                .uri(newHit.getUri())
                .ip(newHit.getIp())
                .timestamp(instant)
                .build();
    }
}

