package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.StatsHitDto;
import ru.practicum.explorewithme.dto.StatsViewDto;
import ru.practicum.explorewithme.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsHitDto createHit(@RequestBody StatsHitDto hitEndpoint) {
        return statsService.createHit(hitEndpoint);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsViewDto> getHits(@RequestParam String start,
                                      @RequestParam String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {

        String startDecoded = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String endDecoded = URLDecoder.decode(end, StandardCharsets.UTF_8);

        return statsService.getHits(startDecoded, endDecoded, uris, unique);
    }

}
