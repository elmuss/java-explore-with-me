package ru.practicum.explorewithme.controller.Private;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.dao.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class RequestControllerPrivate {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable int userId,
                                                 @RequestParam int eventId) {
        return requestService.createRequest(userId, eventId);
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
