package org.example.dto.compilation;

import lombok.Builder;
import lombok.Data;
import org.example.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
public class CompilationDto {
    private Integer id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;
}
