package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.StatsHitDto;
import org.example.model.Stats;

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

