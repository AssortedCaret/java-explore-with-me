package ru.practicum.controller.adminController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.serviceImpl.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminControllerCompilations {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilations(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.addCompilations(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilations(@PathVariable(name = "compId") @Positive Long compId,
                                             @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.updateCompilations(compId, updateCompilationRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilations(@PathVariable(name = "compId") @Positive Long compId) {
        compilationService.deleteCompilations(compId);
    }
}
