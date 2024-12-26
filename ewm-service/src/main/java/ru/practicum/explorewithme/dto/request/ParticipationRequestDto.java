package ru.practicum.explorewithme.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.model.State;

@Data
@Builder
public class ParticipationRequestDto {
    private Integer id;
    private String created;
    private Integer event;
    private Integer requester;
    private State status;
}
