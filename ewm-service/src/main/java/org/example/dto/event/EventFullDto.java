package org.example.dto.event;

import lombok.Builder;
import lombok.Data;
import org.example.dto.category.CategoryDto;
import org.example.dto.location.LocationDto;
import org.example.dto.user.UserShortDto;
import org.example.model.State;

import java.time.Instant;

@Builder
@Data
public class EventFullDto {
    private Integer id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private Instant createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Integer views;
}
