package org.example.dto.event;

import lombok.Builder;
import lombok.Data;
import org.example.model.Location;

@Builder
@Data
public class UpdateEventAdminRequest {
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
