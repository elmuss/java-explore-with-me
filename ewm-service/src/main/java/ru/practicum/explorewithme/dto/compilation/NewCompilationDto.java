package ru.practicum.explorewithme.dto.compilation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NewCompilationDto {
    private String title;
    private Boolean pinned;
    private List<Integer> events;
}
