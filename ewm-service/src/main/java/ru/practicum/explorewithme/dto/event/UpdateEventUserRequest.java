package ru.practicum.explorewithme.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.dto.location.LocationDto;

@Builder
@Data
public class UpdateEventUserRequest {
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
