package ru.practicum.explorewithme.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.State;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private State status;
}
