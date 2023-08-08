package ru.practicum.model.mapper;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.model.Compilation;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.model.mapper.EventMapper.makeEventShortDtoList;

public class CompilationMapper {

    public static CompilationDto makeCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(makeEventShortDtoList(compilation.getEvents()));
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static Compilation makeCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setEvents(List.of());
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());
        return compilation;
    }

    public static Compilation makeCompilation(UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = new Compilation();
        compilation.setId(updateCompilationRequest.getId());
        compilation.setPinned(updateCompilationRequest.getPinned());
        compilation.setTitle(updateCompilationRequest.getTitle());
        return compilation;
    }

    public static List<CompilationDto> makeCompilationDtoList(List<Compilation> compilationList) {
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation compilation : compilationList)
            compilationDtoList.add(makeCompilationDto(compilation));
        return compilationDtoList;
    }
}
