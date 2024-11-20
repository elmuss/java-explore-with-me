package org.example.service;

import org.example.dto.StatsHitDto;
import org.example.dto.StatsViewDto;

import java.util.List;

public interface StatsService {
    StatsHitDto createHit(StatsHitDto hitEndpoint);

    List<StatsViewDto> getHits(String start, String end, List<String> uris, Boolean unique);
}
