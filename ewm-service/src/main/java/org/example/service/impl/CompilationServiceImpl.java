package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.compilation.CompilationDto;
import org.example.dto.compilation.NewCompilationDto;
import org.example.dto.compilation.UpdateCompilationRequest;
import org.example.dto.event.EventShortDto;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;
import org.example.mapper.CompilationMapper;
import org.example.mapper.EventMapper;
import org.example.model.Compilation;
import org.example.model.Event;
import org.example.model.EventsCompilations;
import org.example.repository.CompilationRepository;
import org.example.repository.EventRepository;
import org.example.repository.EventsCompilationsRepository;
import org.example.service.dao.CompilationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventsCompilationsRepository eventsCompilationsRepository;
    private final EventRepository eventRepository;

    private static final String COMPILATION_TITLE_LENGTH_MSG = "Compilation title should not be longer than 50 symbols";
    private static final String COMPILATION_TITLE_EMPTY_MSG = "Compilation title should not be empty";
    private static final String COMPILATION_NOT_FOUND_MSG = "Compilation with id=%d was not found";
    private static final String EVENT_NOT_FOUND_MSG = "Event with id=%d was not found";

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilation) {
        validateCompilation(newCompilation);
        Compilation compilation = CompilationMapper.modelFromNewCompilationDto(newCompilation);

        if (newCompilation.getPinned() == null) {
            compilation.setPinned(false);
        } else {
            compilation.setPinned(newCompilation.getPinned());
        }

        compilationRepository.save(compilation);

        CompilationDto createdCompilation = CompilationMapper.modelToCompilationDto(compilation);

        List<Event> events = new ArrayList<>();

        if (newCompilation.getEvents() != null) {
            for (Integer eventId : newCompilation.getEvents()) {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));
                events.add(event);
            }

            List<EventShortDto> eventShortDto = events.stream().map(EventMapper::modelToEventShortDto).toList();

            createdCompilation.setEvents(eventShortDto);

            for (Integer eventId : newCompilation.getEvents()) {
                EventsCompilations ec = new EventsCompilations();
                ec.setEventId(eventId);
                ec.setCompilationId(createdCompilation.getId());
                eventsCompilationsRepository.save(ec);
            }
        } else {
            createdCompilation.setEvents(List.of());
        }

        return createdCompilation;
    }

    @Override
    public CompilationDto getCompilationById(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND_MSG, compId)));

        CompilationDto returnCompilation = CompilationMapper.modelToCompilationDto(compilation);

        List<Event> events = new ArrayList<>();

        List<Integer> eventIds = eventsCompilationsRepository.findByCompilationId(compId);

        for (Integer eventId : eventIds) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));
            events.add(event);
        }
        List<EventShortDto> eventShortDto = events.stream().map(EventMapper::modelToEventShortDto).toList();
        returnCompilation.setEvents(eventShortDto);

        return returnCompilation;
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> returnCompilation = compilationRepository.findAll().stream()
                .map(CompilationMapper::modelToCompilationDto).limit(size).toList();

        for (CompilationDto c : returnCompilation) {
            List<Event> events = new ArrayList<>();
            List<Integer> eventIds = eventsCompilationsRepository.findByCompilationId(c.getId());
            for (Integer eventId : eventIds) {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));
                events.add(event);
            }
            List<EventShortDto> eventShortDto = events.stream()
                    .map(EventMapper::modelToEventShortDto)
                    .limit(size)
                    .toList();
            c.setEvents(eventShortDto);
        }

        return returnCompilation;
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilation) {
        validateUpdateCompilation(updateCompilation);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPILATION_NOT_FOUND_MSG, compId)));

        eventsCompilationsRepository.deleteByCompilationId(compId);

        Compilation updatedCompilation = CompilationMapper.updateCompilation(compilation, updateCompilation);

        CompilationDto updatedCompilationDto = CompilationMapper.modelToCompilationDto(updatedCompilation);

        List<Event> events = new ArrayList<>();

        if (updateCompilation.getEvents() != null) {
            for (Integer eventId : updateCompilation.getEvents()) {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));
                events.add(event);
            }

            List<EventShortDto> eventShortDto = events.stream().map(EventMapper::modelToEventShortDto).toList();

            updatedCompilationDto.setEvents(eventShortDto);

            for (Integer eventId : updateCompilation.getEvents()) {
                EventsCompilations ec = new EventsCompilations();
                ec.setEventId(eventId);
                ec.setCompilationId(compId);
                eventsCompilationsRepository.save(ec);
            }
        } else {
            for (Integer eventId : eventsCompilationsRepository.findByCompilationId(compId)) {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException(String.format(EVENT_NOT_FOUND_MSG, eventId)));
                events.add(event);
            }

            List<EventShortDto> eventShortDto = events.stream().map(EventMapper::modelToEventShortDto).toList();

            updatedCompilationDto.setEvents(eventShortDto);
        }

        return updatedCompilationDto;
    }

    @Override
    @Transactional
    public void deleteCompilation(Integer compId) {
        compilationRepository.deleteById(compId);
        eventsCompilationsRepository.deleteByCompilationId(compId);
    }

    public void validateCompilation(NewCompilationDto newCompilation) {
        if (newCompilation.getTitle() == null) {
            throw new ValidationException(COMPILATION_TITLE_EMPTY_MSG);
        }
        if (newCompilation.getTitle().isEmpty() || newCompilation.getTitle().isBlank()) {
            throw new ValidationException(COMPILATION_TITLE_EMPTY_MSG);
        }
        if (newCompilation.getTitle().length() > 50) {
            throw new ValidationException(COMPILATION_TITLE_LENGTH_MSG);
        }
    }

    public void validateUpdateCompilation(UpdateCompilationRequest updateCompilation) {
        if (updateCompilation.getTitle() != null && updateCompilation.getTitle().length() > 50) {
            throw new ValidationException(COMPILATION_TITLE_LENGTH_MSG);
        }
    }
}
