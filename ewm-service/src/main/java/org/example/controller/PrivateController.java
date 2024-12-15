package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.event.EventFullDto;
import org.example.dto.event.EventShortDto;
import org.example.dto.event.NewEventDto;
import org.example.dto.event.UpdateEventUserRequest;
import org.example.dto.request.EventRequestStatusUpdateRequest;
import org.example.dto.request.EventRequestStatusUpdateResult;
import org.example.dto.request.ParticipationRequestDto;
import org.example.service.dao.EventService;
import org.example.service.dao.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class PrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable int userId,
                                    @Valid @RequestBody NewEventDto newEvent) {
        return eventService.createEvent(userId, newEvent);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable int userId,
                                                 @RequestParam int eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getUsersEventsByParam(
            @PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getUsersEventsByParam(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getUsersEventById(@PathVariable Integer userId,
                                          @PathVariable Integer eventId) {
        return eventService.getUsersEventById(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUsersEventsRequests(@PathVariable Integer userId,
                                                                @PathVariable Integer eventId) {
        return requestService.getUsersEventsRequests(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getUsersRequests(@PathVariable Integer userId) {
        return requestService.getUsersRequests(userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateUsersEvent(@PathVariable Integer userId,
                                         @PathVariable Integer eventId,
                                         @RequestBody UpdateEventUserRequest updateUserEvent) {
        return eventService.updateUsersEvent(userId, eventId, updateUserEvent);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateUsersEventsRequests(
            @PathVariable Integer userId,
            @PathVariable Integer eventId,
            @RequestBody EventRequestStatusUpdateRequest updateUsersEventsRequests) {
        return requestService.updateUsersEventsRequests(userId, eventId, updateUsersEventsRequests);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelUsersRequest(
            @PathVariable Integer userId,
            @PathVariable Integer requestId) {
        return requestService.cancelUsersRequest(userId, requestId);
    }
}
