package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.StatsHitDto;
import ru.practicum.explore.dto.StatsViewDto;
import ru.practicum.explore.model.Stats;
import ru.practicum.explore.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;
    public static final String DATE = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsHitDto createHit(@RequestBody Stats hitEndpoint) {
        return statsService.createHit(hitEndpoint);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsViewDto> getHits(@DateTimeFormat(pattern = DATE) @RequestParam LocalDateTime start,
                                      @DateTimeFormat(pattern = DATE) @RequestParam LocalDateTime end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {

        return statsService.getHits(start, end, uris, unique);
    }

}
