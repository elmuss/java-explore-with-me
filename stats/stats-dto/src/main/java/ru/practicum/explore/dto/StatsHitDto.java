package ru.practicum.explore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsHitDto {
    private int id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
