package org.example;

import org.example.dto.StatsHitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;


public class StatsHitClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    @Autowired
    public StatsHitClient(@Value("${stats.url}") String serviceUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public void create(StatsHitDto newStatsHitDto) {
        post("", newStatsHitDto);
    }
}
