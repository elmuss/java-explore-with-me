package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.model.Event;

import java.util.Optional;

@UtilityClass
public class EventMapper {
    public static Event modelFromNewEventDto(NewEventDto newEvent) {
        return Event.builder()
                .annotation(newEvent.getAnnotation())
                .description(newEvent.getDescription())
                .eventDate(DateMapper.instantFromString(newEvent.getEventDate()))
                .title(newEvent.getTitle())
                .build();
    }

    public static EventFullDto modelToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .eventDate(DateMapper.stringFromInstant(event.getEventDate()))
                .location(LocationMapper.modelToDto(event.getLocation()))
                .initiator(UserMapper.modelToUserShortDto(event.getInitiator()))
                .category(CategoryMapper.modelToCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto modelToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.modelToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(DateMapper.stringFromInstant(event.getEventDate()))
                .initiator(UserMapper.modelToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event modelToUpdatedEvent(Event oldEvent, UpdateEventAdminRequest updatedEvent) {
        oldEvent.setAnnotation(Optional.ofNullable(updatedEvent.getAnnotation())
                .filter(annotation -> !annotation.isBlank()).orElse(oldEvent.getAnnotation()));
        oldEvent.setDescription(Optional.ofNullable(updatedEvent.getDescription())
                .filter(description -> !description.isBlank()).orElse(oldEvent.getDescription()));
        oldEvent.setTitle(Optional.ofNullable(updatedEvent.getTitle())
                .filter(title -> !title.isBlank()).orElse(oldEvent.getTitle()));

        return oldEvent;
    }

    public static Event modelToUpdatedEventByUser(Event oldEvent, UpdateEventUserRequest updatedEventByUser) {
        oldEvent.setAnnotation(Optional.ofNullable(updatedEventByUser.getAnnotation())
                .filter(annotation -> !annotation.isBlank()).orElse(oldEvent.getAnnotation()));
        oldEvent.setDescription(Optional.ofNullable(updatedEventByUser.getDescription())
                .filter(description -> !description.isBlank()).orElse(oldEvent.getDescription()));
        oldEvent.setTitle(Optional.ofNullable(updatedEventByUser.getTitle())
                .filter(title -> !title.isBlank()).orElse(oldEvent.getTitle()));

        return oldEvent;
    }
}
