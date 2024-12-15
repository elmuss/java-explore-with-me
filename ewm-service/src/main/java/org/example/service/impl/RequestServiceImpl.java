package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.EventRequestStatusUpdateRequest;
import org.example.dto.request.EventRequestStatusUpdateResult;
import org.example.dto.request.ParticipationRequestDto;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.mapper.RequestMapper;
import org.example.model.Event;
import org.example.model.Request;
import org.example.model.State;
import org.example.model.User;
import org.example.repository.EventRepository;
import org.example.repository.RequestRepository;
import org.example.repository.UserRepository;
import org.example.service.dao.RequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    private static final String EVENT_NOT_FOUND_MSG = "Event with id=%d was not found";
    private static final String USER_NOT_FOUND_MSG = "User with id=%d was not found";
    private static final String REPEATED_REQUEST_NOT_ALLOWED_MSG = "Unable to resend the same request";
    private static final String REQUEST_ON_USERS_EVENT_NOT_ALLOWED_MSG = "Unable to send request on your event";
    private static final String REQUEST_ON_UNPUBLISHED_EVENT_NOT_ALLOWED_MSG = "Unable to send request on unpublished event";
    private static final String LIMIT_OF_PARTICIPANTS_EXCEEDED_MSG = "Limit of participants exceeded";
    private static final String REQUEST_ALREADY_CONFIRMED_MSG = "Request is already confirmed";

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        if (requestRepository.getByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException(REPEATED_REQUEST_NOT_ALLOWED_MSG);
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_MSG));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));

        validateRequest(event, user);

        Request request = new Request();
        request.setCreated(Instant.now());
        request.setEvent(event);
        request.setRequester(user);

        if (event.getParticipantLimit() == 0) {
            request.setStatus(State.CONFIRMED);
        } else {
            request.setStatus(State.PENDING);
        }

        requestRepository.save(request);

        return RequestMapper.modelToParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUsersEventsRequests(Integer userId, Integer eventId) {
        List<Request> requestList = requestRepository.findByEventId(eventId);

        if (requestList.isEmpty()) {
            return List.of();
        } else {
            return requestList.stream().map(RequestMapper::modelToParticipationRequestDto).toList();
        }
    }

    @Override
    public List<ParticipationRequestDto> getUsersRequests(Integer userId) {
        List<Request> requestList = requestRepository.findByRequesterId(userId);

        if (requestList.isEmpty()) {
            return List.of();
        } else {
            return requestList.stream().map(RequestMapper::modelToParticipationRequestDto).toList();
        }
    }

    public void validateRequest(Event event, User user) {
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException(REQUEST_ON_USERS_EVENT_NOT_ALLOWED_MSG);
        }

        if (event.getState().equals(State.PENDING)) {
            throw new ConflictException(REQUEST_ON_UNPUBLISHED_EVENT_NOT_ALLOWED_MSG);
        }

        if (event.getParticipantLimit() != 0) {
            List<Request> requests = requestRepository.findByEventId(event.getId());

            if (event.getParticipantLimit().equals(requests.size())) {
                throw new ConflictException(LIMIT_OF_PARTICIPANTS_EXCEEDED_MSG);
            }
        }
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateUsersEventsRequests(
            Integer userId, Integer eventId, EventRequestStatusUpdateRequest updateUsersEventsRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));

        List<Request> requests = requestRepository.findAllById(updateUsersEventsRequest.getRequestIds());

        for (Request request : requests) {
            if (updateUsersEventsRequest.getStatus().equals(State.REJECTED)
                    && request.getStatus().equals(State.CONFIRMED)) {
                throw new ConflictException(REQUEST_ALREADY_CONFIRMED_MSG);
            } else {
                request.setStatus(updateUsersEventsRequest.getStatus());
                requestRepository.save(request);
            }
        }

        List<Request> requestsUpdated = requestRepository.findAllById(updateUsersEventsRequest.getRequestIds());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (Request r : requestsUpdated) {
            if (r.getStatus().equals(State.CONFIRMED)) {
                confirmedRequests.add(RequestMapper.modelToParticipationRequestDto(r));
            } else if (r.getStatus().equals(State.REJECTED)) {
                rejectedRequests.add(RequestMapper.modelToParticipationRequestDto(r));
            }
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelUsersRequest(Integer userId, Integer requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId);
        request.setStatus(State.CANCELED);

        return RequestMapper.modelToParticipationRequestDto(request);
    }
}
