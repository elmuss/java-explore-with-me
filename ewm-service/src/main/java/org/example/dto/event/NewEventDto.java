package org.example.dto.event;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.example.model.Location;

@Builder
@Data
public class NewEventDto {
    @NonNull
    private String annotation;
    private Integer category;
    @NonNull
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
