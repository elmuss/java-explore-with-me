package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.compilation.CompilationDto;
import org.example.dto.compilation.NewCompilationDto;
import org.example.dto.compilation.UpdateCompilationRequest;
import org.example.model.Compilation;

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

    public static Compilation updateCompilation(Compilation compilation, UpdateCompilationRequest updateCompilation) {
        if (updateCompilation.getPinned() != null) {
            compilation.setPinned(updateCompilation.getPinned());
        }

        if (updateCompilation.getTitle() != null) {
            compilation.setTitle(updateCompilation.getTitle());
        }

        return compilation;
    }
}
