package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.StatsHitDto;
import org.example.dto.StatsViewDto;
import org.example.mapper.StatsHitMapper;
import org.example.model.Stats;
import org.example.repository.StatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository sr;

    @Override
    @Transactional
    public StatsHitDto createHit(StatsHitDto hitEndpoint) {

        return StatsHitMapper.modelToDto(sr.save(StatsHitMapper.modelFromDto(hitEndpoint)));
    }

    @Override
    public List<StatsViewDto> getHits(String start, String end, List<String> uris, Boolean unique) {
        List<StatsViewDto> hits = new ArrayList<>();
        List<Stats> stats = new ArrayList<>();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime ldtStart = LocalDateTime.parse(start, dtf);
        ZonedDateTime zdtStart = ZonedDateTime.of(ldtStart, ZoneId.of("UTC+0"));
        Instant instantStart = Instant.from(zdtStart);

        LocalDateTime ldtEnd = LocalDateTime.parse(end, dtf);
        ZonedDateTime zdtEnd = ZonedDateTime.of(ldtEnd, ZoneId.of("UTC+0"));
        Instant instantEnd = Instant.from(zdtEnd);

        if (uris == null && !unique) {
            stats.addAll(sr.findAllByTimestamp(instantStart, instantEnd));

        } else if (uris != null && !unique) {
            for (String uri : uris) {
                stats.addAll(sr.findByUriAndTimestamp(uri, instantStart, instantEnd));
            }
        } else if (uris == null) {
            stats.addAll(sr.findAllByTimestampUnique(instantStart, instantEnd));
        } else {
            for (String uri : uris) {
                List<Stats> statsToAdd = sr.findByUriAndTimestampUnique(uri, instantStart, instantEnd);
                for (Stats s : statsToAdd) {
                    if (stats.isEmpty()) {
                        stats.add(s);
                    } else {
                        if ((!Objects.equals(s.getIp(), stats.getLast().getIp()))
                                && !Objects.equals(s.getTimestamp(), stats.getLast().getTimestamp())) {
                            stats.add(s);
                        }
                    }
                }
            }
        }

        Map<String, List<Stats>> uriToHit = stats.stream().collect(Collectors.groupingBy(Stats::getUri));

        for (Map.Entry<String, List<Stats>> uriHit : uriToHit.entrySet()) {
            hits.add(new StatsViewDto(uriHit.getValue().getFirst().getApp(), uriHit.getKey(), uriHit.getValue().size()));
        }

        return hits.stream().sorted(Comparator.comparing(StatsViewDto::getHits)).toList().reversed();
    }
}
