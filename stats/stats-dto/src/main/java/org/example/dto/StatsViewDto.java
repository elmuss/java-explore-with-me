package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsViewDto {
    private String app;
    private String uri;
    private Integer hits;
}
