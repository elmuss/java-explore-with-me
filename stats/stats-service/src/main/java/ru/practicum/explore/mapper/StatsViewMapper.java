package ru.practicum.explore.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.StatsViewDto;
import ru.practicum.explore.model.StatsCount;

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
