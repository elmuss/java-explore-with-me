package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.StatsHitDto;
import ru.practicum.explore.dto.StatsViewDto;
import ru.practicum.explore.mapper.StatsHitMapper;
import ru.practicum.explore.mapper.StatsViewMapper;
import ru.practicum.explore.model.Stats;
import ru.practicum.explore.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository sr;

    @Override
    @Transactional
    public StatsHitDto createHit(Stats hitEndpoint) {
        return StatsHitMapper.modelToDto(sr.save(hitEndpoint));
    }

    @Override
    public List<StatsViewDto> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatsViewDto> statsView = new ArrayList<>();

        if (uris == null && unique) {
            uris = getAllUris();

            for (String uri : uris) {
                statsView.add(StatsViewMapper.modelToDto(sr.countHU(uri, start, end)));
            }

        } else if (uris == null) {
            uris = getAllUris();

            for (String uri : uris) {
                statsView.add(StatsViewMapper.modelToDto(sr.countH(uri, start, end)));
            }

        } else if (unique) {
            for (String uri : uris) {
                statsView.add(StatsViewMapper.modelToDto(sr.countHU(uri, start, end)));
            }

        } else {
            for (String uri : uris) {
                statsView.add(StatsViewMapper.modelToDto(sr.countH(uri, start, end)));
            }
        }


        return statsView.stream().sorted((f1, f2) -> Integer.compare(f2.getHits(), f1.getHits())).toList();
    }

    public List<String> getAllUris() {
        List<Stats> allStats;
        List<String> allUris = new ArrayList<>();
        allStats = sr.findAll();

        for (Stats s : allStats) {
            allUris.add(s.getUri());
        }

        return allUris.stream().distinct().toList();
    }
}
