package org.example.mapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.example.dto.StatsHitDto;
import org.example.dto.event.*;
import org.example.model.Event;
import org.example.model.State;

import java.time.Instant;
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
        if (updatedEvent.getEventDate() != null) {
            oldEvent.setEventDate(DateMapper.instantFromString(updatedEvent.getEventDate()));
        }
        if (updatedEvent.getLocation() != null) {
            oldEvent.setLocation(updatedEvent.getLocation());
        }
        if (updatedEvent.getPaid() != null) {
            oldEvent.setPaid(updatedEvent.getPaid());
        }
        if (updatedEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updatedEvent.getParticipantLimit());
        }
        if (updatedEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updatedEvent.getRequestModeration());
        }

        return oldEvent;
    }

    public static Event modelToUpdatedEventByUser(Event oldEvent, UpdateEventUserRequest updatedEventByUser) {
        oldEvent.setAnnotation(Optional.ofNullable(updatedEventByUser.getAnnotation())
                .filter(annotation -> !annotation.isBlank()).orElse(oldEvent.getAnnotation()));
        oldEvent.setDescription(Optional.ofNullable(updatedEventByUser.getDescription())
                .filter(description -> !description.isBlank()).orElse(oldEvent.getDescription()));
        oldEvent.setTitle(Optional.ofNullable(updatedEventByUser.getTitle())
                .filter(title -> !title.isBlank()).orElse(oldEvent.getTitle()));
        if (updatedEventByUser.getEventDate() != null) {
            oldEvent.setEventDate(DateMapper.instantFromString(updatedEventByUser.getEventDate()));
        }
        if (updatedEventByUser.getLocation() != null) {
            oldEvent.setLocation(updatedEventByUser.getLocation());
        }
        if (updatedEventByUser.getPaid() != null) {
            oldEvent.setPaid(updatedEventByUser.getPaid());
        }
        if (updatedEventByUser.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updatedEventByUser.getParticipantLimit());
        }
        if (updatedEventByUser.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updatedEventByUser.getRequestModeration());
        }
        if (updatedEventByUser.getStateAction() != null) {
            if (updatedEventByUser.getStateAction().equals("SEND_TO_REVIEW")) {
                oldEvent.setState(State.PENDING);
            } else if (updatedEventByUser.getStateAction().equals("CANCEL_REVIEW")) {
                oldEvent.setState(State.CANCELED);
            }
        }
        return oldEvent;
    }

    public static StatsHitDto modelToStatsHitDto(HttpServletRequest request) {
        return StatsHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(DateMapper.stringFromInstant(Instant.now()))
                .build();
    }
}
