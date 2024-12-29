package ru.practicum.explorewithme.service.dao;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.explorewithme.dto.event.*;

import java.util.List;

public interface EventService {
    EventFullDto createEvent(int userId, NewEventDto newEvent);

    List<EventFullDto> getEventsByParam(HttpServletRequest request,
                                        List<Integer> userIds,
                                        List<String> states,
                                        List<Integer> categoryIds,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size);

    EventFullDto getEventById(Integer id, HttpServletRequest request);

    List<EventShortDto> getEventsByParamPublic(HttpServletRequest request,
                                               String text, List<Integer> categories, Boolean paid,
                                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                               String sort, Integer from, Integer size);

    EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequest updateEvent);

    List<EventShortDto> getUsersEventsByParam(Integer userId, Integer from, Integer size);

    EventFullDto getUsersEventById(Integer userId, Integer eventId);

    EventFullDto updateUsersEvent(Integer userId, Integer eventId, UpdateEventUserRequest updateUserEvent);
}
