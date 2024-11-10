package ru.practicum.explore.service;

import ru.practicum.explore.dto.StatsHitDto;
import ru.practicum.explore.dto.StatsViewDto;
import ru.practicum.explore.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsHitDto createHit(Stats hitEndpoint);

    List<StatsViewDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
