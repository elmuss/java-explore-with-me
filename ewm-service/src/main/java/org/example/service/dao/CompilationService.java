package org.example.service.dao;

import org.example.dto.compilation.CompilationDto;
import org.example.dto.compilation.NewCompilationDto;
import org.example.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilation);

    CompilationDto getCompilationById(Integer compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest updateCompilation);

    void deleteCompilation(Integer compId);
}
