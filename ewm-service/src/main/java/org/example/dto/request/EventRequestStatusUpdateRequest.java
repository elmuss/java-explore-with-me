package org.example.dto.request;

import lombok.Builder;
import lombok.Data;
import org.example.model.State;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private State status;
}
