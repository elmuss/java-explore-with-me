package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.request.ParticipationRequestDto;
import org.example.model.Request;

@UtilityClass
public class RequestMapper {
    public static ParticipationRequestDto modelToParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(DateMapper.stringFromInstant(request.getCreated()))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
