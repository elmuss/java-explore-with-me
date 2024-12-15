package org.example.service.dao;

import org.example.dto.request.EventRequestStatusUpdateRequest;
import org.example.dto.request.EventRequestStatusUpdateResult;
import org.example.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(int userId, int eventId);

    List<ParticipationRequestDto> getUsersEventsRequests(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getUsersRequests(Integer userId);

    EventRequestStatusUpdateResult updateUsersEventsRequests(Integer userId, Integer eventId, EventRequestStatusUpdateRequest updateUsersEventsRequests);

    ParticipationRequestDto cancelUsersRequest(Integer userId, Integer requestId);
}
