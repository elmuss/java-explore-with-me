package ru.practicum.explorewithme.controller.Private;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.dao.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class EventControllerPrivate {
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable int userId,
                                    @Valid @RequestBody NewEventDto newEvent) {
        return eventService.createEvent(userId, newEvent);
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

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateUsersEvent(@PathVariable Integer userId,
                                         @PathVariable Integer eventId,
                                         @RequestBody UpdateEventUserRequest updateUserEvent) {
        return eventService.updateUsersEvent(userId, eventId, updateUserEvent);
    }
}
