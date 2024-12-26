package ru.practicum.explorewithme.dto.compilation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateCompilationRequest {
    private List<Integer> events;
    private Boolean pinned;
    private String title;
}
