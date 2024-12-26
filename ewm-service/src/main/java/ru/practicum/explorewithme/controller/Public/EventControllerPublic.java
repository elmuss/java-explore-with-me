package ru.practicum.explorewithme.controller.Public;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.impl.EventServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventControllerPublic {
    private final EventServiceImpl eventService;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByParamPublic(HttpServletRequest request,
                                                      @RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Integer> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) String rangeStart,
                                                      @RequestParam(required = false) String rangeEnd,
                                                      @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(required = false) String sort,
                                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsByParamPublic(
                request, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable Integer id, HttpServletRequest request) {
        return eventService.getEventById(id, request);
    }
}
