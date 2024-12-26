package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventsCompilations;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.EventsCompilationsRepository;
import ru.practicum.explorewithme.service.dao.CompilationService;

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

    private static final Integer COMPILATION_NAME_MAX_LENGTH = 50;
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

        List<Integer> eventIds = eventsCompilationsRepository.findByCompilationId(compId);

        List<Event> events = eventRepository.findAllById(eventIds);

        List<EventShortDto> eventShortDto = events.stream().map(EventMapper::modelToEventShortDto).toList();
        returnCompilation.setEvents(eventShortDto);

        return returnCompilation;
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<CompilationDto> returnCompilation = compilationRepository.findAll(PageRequest.of(from, size)).stream()
                .map(CompilationMapper::modelToCompilationDto).toList();

        for (CompilationDto c : returnCompilation) {
            List<Integer> eventIds = eventsCompilationsRepository.findByCompilationId(c.getId());
            List<Event> events = eventRepository.findAllById(eventIds);
            List<EventShortDto> eventShortDto = events.stream()
                    .map(EventMapper::modelToEventShortDto)
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

        if (updateCompilation.getPinned() != null) {
            compilation.setPinned(updateCompilation.getPinned());
        }

        if (updateCompilation.getTitle() != null) {
            compilation.setTitle(updateCompilation.getTitle());
        }

        CompilationDto updatedCompilationDto = CompilationMapper.modelToCompilationDto(compilation);

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
        if (newCompilation.getTitle().length() > COMPILATION_NAME_MAX_LENGTH) {
            throw new ValidationException(COMPILATION_TITLE_LENGTH_MSG);
        }
    }

    public void validateUpdateCompilation(UpdateCompilationRequest updateCompilation) {
        if (updateCompilation.getTitle() != null
                && updateCompilation.getTitle().length() > COMPILATION_NAME_MAX_LENGTH) {
            throw new ValidationException(COMPILATION_TITLE_LENGTH_MSG);
        }
    }
}
