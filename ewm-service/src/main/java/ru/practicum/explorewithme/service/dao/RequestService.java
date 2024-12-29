package ru.practicum.explorewithme.service.dao;

import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(int userId, int eventId);

    List<ParticipationRequestDto> getUsersEventsRequests(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getUsersRequests(Integer userId);

    EventRequestStatusUpdateResult updateUsersEventsRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest updateUsersEventsRequests);

    ParticipationRequestDto cancelUsersRequest(Integer userId, Integer requestId);
}
