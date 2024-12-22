package org.example.service.impl;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.StatsClient;
import org.example.dto.event.*;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;
import org.example.mapper.CategoryMapper;
import org.example.mapper.DateMapper;
import org.example.mapper.EventMapper;
import org.example.mapper.StateMapper;
import org.example.model.*;
import org.example.repository.*;
import org.example.service.dao.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final StatsClient statsClient;

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    private static final String CATEGORY_NOT_FOUND_MSG = "Category with id=%d was not found";
    private static final String USER_NOT_FOUND_MSG = "User with id=%d was not found";
    private static final String EVENT_NOT_FOUND_MSG = "Event with id=%d was not found";
    private static final String EVENT_DESCRIPTION_EMPTY_MSG = "Description should not be empty";
    private static final String EVENT_ANNOTATION_EMPTY_MSG = "Annotation should not be empty";
    private static final String EVENT_NEGATIVE_PARTICIPANTS_LIMIT_MSG = "Participants limit should not be negative";
    private static final String EVENT_DESCRIPTION_LENGTH_MSG = "Event description should be be between 20 and 7000";
    private static final String EVENT_ANNOTATION_LENGTH_MSG = "Event annotation length should be between 20 and 2000";
    private static final String EVENT_TITLE_LENGTH_MSG = "Event title length should be between 3 and 120";
    private static final String EVENT_WRONG_DATE_MSG =
            "Event date should not be earlier, than 2 hours past current date and time";
    private static final String EVENT_ALREADY_PUBLISHED_MSG = "Event is already published";
    private static final String EVENT_CANCELED_MSG = "Event is canceled";
    private static final String END_TIME_WRONG_MSG = "End time is not correct";

    @Override
    @Transactional
    public EventFullDto createEvent(int userId, NewEventDto newEvent) {
        validateEvent(newEvent);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        Category category = categoryRepository.findById(newEvent.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MSG, newEvent.getCategory())));

        Event event = EventMapper.modelFromNewEventDto(newEvent);
        event.setCategory(category);
        event.setLocation(locationRepository.save(newEvent.getLocation()));
        event.setConfirmedRequests(0);
        event.setCreatedOn(Instant.now());
        event.setInitiator(user);
        event.setState(State.PENDING);
        event.setViews(0);

        if (newEvent.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(newEvent.getPaid());
        }

        if (newEvent.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(newEvent.getParticipantLimit());
        }

        if (newEvent.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(newEvent.getRequestModeration());
        }

        eventRepository.save(event);

        return EventMapper.modelToEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getEventsByParam(List<Integer> userIds, List<String> states,
                                               List<Integer> catIds, String rangeStart,
                                               String rangeEnd, Integer from, Integer size) {
        List<Event> events = new ArrayList<>();
        BooleanBuilder params = new BooleanBuilder();

        if (Objects.nonNull(catIds)) {
            params.and(QEvent.event.category.id.in(catIds));
        }

        if (Objects.nonNull(userIds)) {
            params.and(QEvent.event.initiator.id.in(userIds));
        }

        if (Objects.nonNull(states)) {
            List<State> s = states.stream().map(StateMapper::stateConvert).toList();
            params.and(QEvent.event.state.in(s));
        }

        if (Objects.nonNull(rangeStart)) {
            Instant start = DateMapper.instantFromString(rangeStart);
            params.and(QEvent.event.eventDate.after(start));
        }

        if (Objects.nonNull(rangeEnd)) {
            Instant end = DateMapper.instantFromString(rangeEnd);
            params.and(QEvent.event.eventDate.before(end));
        }

        Iterable<Event> foundEvents = eventRepository.findAll(params);
        for (Event e : foundEvents) {
            e.setConfirmedRequests(requestRepository.findByEventIdAndStatusLike(e.getId(), State.CONFIRMED).size());
            events.add(e);
        }

        return events.stream().map(EventMapper::modelToEventFullDto).limit(size).toList();
    }

    @Override
    public EventFullDto getEventById(Integer id, HttpServletRequest request) {
        statsClient.create(EventMapper.modelToStatsHitDto(request));
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty() || event.get().getState() != State.PUBLISHED) {
            throw new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, id));
        }

        Event foundEvent = event.get();

        String timeForStat = DateMapper.stringFromInstant(foundEvent.getPublishedOn());
        List<String> urisForStat = new ArrayList<>();
        urisForStat.add(request.getRequestURI());

        foundEvent.setViews(statsClient
                .getHits(timeForStat, timeForStat, urisForStat, true)
                .getFirst()
                .getHits());

        return EventMapper.modelToEventFullDto(foundEvent);
    }

    @Override
    public List<EventShortDto> getEventsByParamPublic(
            String text, List<Integer> categories,
            Boolean paid, String rangeStart, String rangeEnd,
            Boolean onlyAvailable, String sort, Integer from, Integer size) {

        List<Event> events = new ArrayList<>();
        BooleanBuilder params = new BooleanBuilder();

        if (onlyAvailable) {
            params.and(QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit));
        }

        if (Objects.nonNull(categories)) {
            params.and(QEvent.event.category.id.in(categories));
        }

        if (Objects.nonNull(text)) {
            params.and(QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text)));
        }

        if (Objects.nonNull(paid)) {
            params.and(QEvent.event.paid.isTrue());
        }

        if (Objects.nonNull(rangeStart)) {
            Instant start = DateMapper.instantFromString(rangeStart);
            params.and(QEvent.event.eventDate.after(start));
        }

        if (Objects.nonNull(rangeEnd)) {
            if (Objects.nonNull(rangeStart)
                    && DateMapper.instantFromString(rangeEnd).isBefore(DateMapper.instantFromString(rangeStart))) {
                throw new ValidationException(END_TIME_WRONG_MSG);
            }
            Instant end = DateMapper.instantFromString(rangeEnd);
            params.and(QEvent.event.eventDate.before(end));
        }

        if (rangeStart == null && rangeEnd == null) {
            params.and(QEvent.event.eventDate.after(Instant.now()));
        }

        Iterable<Event> foundEvents = eventRepository.findAll(params);
        for (Event e : foundEvents) {
            events.add(e);
        }

        if (Objects.nonNull(sort)) {
            if (sort.equals("EVENT_DATE")) {
                events = events.stream()
                        .limit(size)
                        .sorted(Comparator.comparing(Event::getEventDate))
                        .toList();
            } else if (sort.equals("VIEWS")) {
                events = events.stream()
                        .limit(size)
                        .sorted(Comparator.comparingInt(Event::getViews))
                        .toList();
            }
        }

        return events.stream().map(EventMapper::modelToEventShortDto).limit(size).toList();
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequest updateEvent) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));

        validateUpdateEvent(updateEvent);

        Event newEvent = EventMapper.modelToUpdatedEvent(oldEvent, updateEvent);

        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals("PUBLISH_EVENT")) {
                if (oldEvent.getState().equals(State.PUBLISHED)) {
                    throw new ConflictException(EVENT_ALREADY_PUBLISHED_MSG);
                } else if (oldEvent.getState().equals(State.REJECTED)) {
                    throw new ConflictException(EVENT_CANCELED_MSG);
                } else {
                    newEvent.setState(State.PUBLISHED);
                    newEvent.setPublishedOn(Instant.now());
                }

            } else if (updateEvent.getStateAction().equals("REJECT_EVENT")) {
                if (oldEvent.getState().equals(State.PUBLISHED)) {
                    throw new ConflictException(EVENT_ALREADY_PUBLISHED_MSG);
                } else {
                    newEvent.setState(State.REJECTED);
                }
            }
        }

        eventRepository.save(newEvent);
        locationRepository.save(newEvent.getLocation());

        EventFullDto updatedEvent = EventMapper.modelToEventFullDto(newEvent);

        if (newEvent.getPublishedOn() != null) {
            updatedEvent.setPublishedOn(DateMapper.stringFromInstant(newEvent.getPublishedOn()));
        }

        if (updateEvent.getCategory() != null) {
            Category newCategory = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format(CATEGORY_NOT_FOUND_MSG, updateEvent.getCategory())));
            updatedEvent.setCategory(CategoryMapper.modelToCategoryDto(newCategory));
        }
        return updatedEvent;
    }

    @Override
    public List<EventShortDto> getUsersEventsByParam(Integer userId, Integer from, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        List<Event> events = eventRepository.findByInitiatorId(userId);

        return events.stream().map(EventMapper::modelToEventShortDto).limit(size).toList();
    }

    @Override
    public EventFullDto getUsersEventById(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));

        return EventMapper.modelToEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId));
    }

    @Override
    @Transactional
    public EventFullDto updateUsersEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateUserEvent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(EVENT_ALREADY_PUBLISHED_MSG);
        }

        validateUpdateUserEvent(updateUserEvent);
        Event newEvent = EventMapper.modelToUpdatedEventByUser(event, updateUserEvent);
        eventRepository.save(newEvent);

        return EventMapper.modelToEventFullDto(newEvent);
    }

    public void validateEvent(NewEventDto newEvent) {
        if (newEvent.getDescription().isEmpty() || newEvent.getDescription().isBlank()) {
            throw new ValidationException(EVENT_DESCRIPTION_EMPTY_MSG);
        }
        if (newEvent.getAnnotation().isEmpty() || newEvent.getAnnotation().isBlank()) {
            throw new ValidationException(EVENT_ANNOTATION_EMPTY_MSG);
        }
        if (newEvent.getDescription().length() < 20 || newEvent.getDescription().length() > 7000) {
            throw new ValidationException(EVENT_DESCRIPTION_LENGTH_MSG);
        }
        if (newEvent.getAnnotation().length() < 20 || newEvent.getAnnotation().length() > 2000) {
            throw new ValidationException(EVENT_ANNOTATION_LENGTH_MSG);
        }
        if (newEvent.getTitle().length() < 3 || newEvent.getTitle().length() > 120) {
            throw new ValidationException(EVENT_TITLE_LENGTH_MSG);
        }
        if (newEvent.getParticipantLimit() != null && newEvent.getParticipantLimit() < 0) {
            throw new ValidationException(EVENT_NEGATIVE_PARTICIPANTS_LIMIT_MSG);
        }
        if (DateMapper.instantFromString(newEvent.getEventDate()).isBefore(Instant.now().plusSeconds(3600))) {
            throw new ValidationException(EVENT_WRONG_DATE_MSG);
        }
    }

    public void validateUpdateEvent(UpdateEventAdminRequest updateEvent) {
        if (updateEvent.getParticipantLimit() != null && updateEvent.getParticipantLimit() < 0) {
            throw new ValidationException(EVENT_NEGATIVE_PARTICIPANTS_LIMIT_MSG);
        }
        if (updateEvent.getTitle() != null &&
                (updateEvent.getTitle().length() < 3 || updateEvent.getTitle().length() > 120)) {
            throw new ValidationException(EVENT_TITLE_LENGTH_MSG);

        }
        if (updateEvent.getDescription() != null &&
                (updateEvent.getDescription().length() < 20 || updateEvent.getDescription().length() > 7000)) {
            throw new ValidationException(EVENT_DESCRIPTION_LENGTH_MSG);
        }
        if (updateEvent.getAnnotation() != null &&
                (updateEvent.getAnnotation().length() < 20 || updateEvent.getAnnotation().length() > 2000)) {
            throw new ValidationException(EVENT_ANNOTATION_LENGTH_MSG);
        }
        if (updateEvent.getEventDate() != null &&
                DateMapper.instantFromString(updateEvent.getEventDate()).isBefore(Instant.now())) {
            throw new ValidationException(EVENT_WRONG_DATE_MSG);
        }
    }

    public void validateUpdateUserEvent(UpdateEventUserRequest updateUserEvent) {
        if (updateUserEvent.getParticipantLimit() != null && updateUserEvent.getParticipantLimit() < 0) {
            throw new ValidationException(EVENT_NEGATIVE_PARTICIPANTS_LIMIT_MSG);
        }
        if (updateUserEvent.getTitle() != null &&
                (updateUserEvent.getTitle().length() < 3 || updateUserEvent.getTitle().length() > 120)) {
            throw new ValidationException(EVENT_TITLE_LENGTH_MSG);
        }
        if (updateUserEvent.getDescription() != null &&
                (updateUserEvent.getDescription().length() < 20 || updateUserEvent.getDescription().length() > 7000)) {
            throw new ValidationException(EVENT_DESCRIPTION_LENGTH_MSG);
        }
        if (updateUserEvent.getAnnotation() != null &&
                (updateUserEvent.getAnnotation().length() < 20 || updateUserEvent.getAnnotation().length() > 2000)) {
            throw new ValidationException(EVENT_ANNOTATION_LENGTH_MSG);
        }
        if (updateUserEvent.getEventDate() != null &&
                DateMapper.instantFromString(updateUserEvent.getEventDate()).isBefore(Instant.now())) {
            throw new ValidationException(EVENT_WRONG_DATE_MSG);
        }
    }
}
