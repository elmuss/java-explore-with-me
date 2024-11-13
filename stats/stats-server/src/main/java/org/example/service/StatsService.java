package org.example.service;

import org.example.dto.StatsHitDto;
import org.example.dto.StatsViewDto;
import org.example.model.Stats;

import java.util.List;

public interface StatsService {
    StatsHitDto createHit(Stats hitEndpoint);

    List<StatsViewDto> getHits(String start, String end, List<String> uris, Boolean unique);
}
