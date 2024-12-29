package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.StatsHitDto;
import ru.practicum.explorewithme.dto.StatsViewDto;

import java.util.List;

public interface StatsService {
    StatsHitDto createHit(StatsHitDto hitEndpoint);

    List<StatsViewDto> getHits(String start, String end, List<String> uris, Boolean unique);
}
