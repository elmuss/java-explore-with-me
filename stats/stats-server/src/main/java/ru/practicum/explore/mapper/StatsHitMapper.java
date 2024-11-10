package ru.practicum.explore.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.StatsHitDto;
import ru.practicum.explore.model.Stats;

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
}
