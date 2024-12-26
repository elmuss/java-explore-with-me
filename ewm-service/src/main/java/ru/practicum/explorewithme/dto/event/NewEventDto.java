package ru.practicum.explorewithme.dto.event;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.explorewithme.dto.location.LocationDto;

@Builder
@Data
public class NewEventDto {
    @NonNull
    private String annotation;
    private Integer category;
    @NonNull
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
