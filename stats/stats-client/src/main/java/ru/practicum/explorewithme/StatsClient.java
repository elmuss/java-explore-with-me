package ru.practicum.explorewithme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explorewithme.dto.StatsHitDto;
import ru.practicum.explorewithme.dto.StatsViewDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class StatsClient {
    protected final RestTemplate rest;
    @Value("${stats.url}")
    String statsUrl;

    @Autowired
    public StatsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public void create(StatsHitDto newStatsHitDto) {
        HttpEntity<StatsHitDto> request = new HttpEntity<>(newStatsHitDto);
        rest.postForEntity(statsUrl + "/hit", request, Void.class);
    }

    public List<StatsViewDto> getHits(String start, String end, List<String> uris, Boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(statsUrl + "/stats")
                .queryParam("start", start).encode(StandardCharsets.UTF_8)
                .queryParam("end", end).encode(StandardCharsets.UTF_8)
                .queryParam("uris", uris)
                .queryParam("unique", unique);
        ResponseEntity<List<StatsViewDto>> response = rest.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StatsViewDto>>() {
                }
        );

        return response.getBody();
    }
}
