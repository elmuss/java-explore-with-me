package org.example.dto.event;

import lombok.Builder;
import lombok.Data;
import org.example.dto.category.CategoryDto;
import org.example.dto.user.UserShortDto;

@Builder
@Data
public class EventShortDto {
    private Integer id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
