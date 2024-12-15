package org.example.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsHitDto {
    private int id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
