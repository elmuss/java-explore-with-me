package org.example.dto.request;

import lombok.Builder;
import lombok.Data;
import org.example.model.State;

@Data
@Builder
public class ParticipationRequestDto {
    private Integer id;
    private String created;
    private Integer event;
    private Integer requester;
    private State status;
}
