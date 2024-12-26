package ru.practicum.explorewithme.service.dao;

import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilation);

    CompilationDto getCompilationById(Integer compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilation);

    void deleteCompilation(Integer compId);
}
