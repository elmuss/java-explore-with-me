package org.example.service;

import org.example.dto.StatsHitDto;
import org.example.dto.StatsViewDto;
import org.example.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsHitDto createHit(Stats hitEndpoint);

    List<StatsViewDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
