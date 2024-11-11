package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.StatsViewDto;
import org.example.model.StatsCount;

@UtilityClass
public class StatsViewMapper {
    public static StatsViewDto modelToDto(StatsCount statsCount) {
        return StatsViewDto.builder()
                .app(statsCount.getApp())
                .uri(statsCount.getUri())
                .hits(statsCount.getHits())
                .build();
    }
}
