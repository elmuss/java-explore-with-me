package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.model.Compilation;

@UtilityClass
public class CompilationMapper {
    public static Compilation modelFromNewCompilationDto(NewCompilationDto newCompilation) {
        return Compilation.builder()
                .title(newCompilation.getTitle())
                .build();
    }

    public static CompilationDto modelToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }
}
