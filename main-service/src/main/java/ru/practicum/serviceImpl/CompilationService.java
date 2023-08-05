package ru.practicum.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.exception.NoFoundObjectException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.compilation.CompilationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static ru.practicum.model.mapper.CompilationMapper.*;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final PublicServiceEvent eventService;

    @Transactional
    public CompilationDto addCompilations(NewCompilationDto request) {
        Compilation compilation = makeCompilation(request);
        if (!Objects.nonNull(request.getPinned())) {
            compilation.setPinned(false);
        }

        if (Objects.nonNull(request.getEvents())) {
            List<Event> getEvent = eventService.getAllEventsByIdIn((Set<Long>) request.getEvents());
            compilation.setEvents(getEvent);
        }

        Compilation savedCompilation = compilationRepository.save(compilation);
        return makeCompilationDto(savedCompilation);
    }

    @Transactional
    public CompilationDto updateCompilations(Long compilationId, UpdateCompilationRequest request) {
        Compilation foundCompilation = getCompilationByIdIfExist(compilationId);

        if (Objects.nonNull(request.getTitle())) {
            foundCompilation.setTitle(request.getTitle());
        }

        if (Objects.nonNull(request.getPinned())) {
            foundCompilation.setPinned(false);
        }

        if (Objects.nonNull(request.getEvents())) {
            List<Event> foundEvents = eventService.getAllEventsByIdIn((Set<Long>) request.getEvents());
            foundCompilation.setEvents(foundEvents);
        }

        Compilation savedCompilation = compilationRepository.save(foundCompilation);
        return makeCompilationDto(savedCompilation);
    }

    @Transactional
    public void deleteCompilations(Long compilationId) {
        checkExistCompilationById(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        return makeCompilationDtoList(compilations);
    }

    public CompilationDto getCompilationsId(Long compilationId) {
        Compilation foundCompilation = getCompilationByIdIfExist(compilationId);
        return makeCompilationDto(foundCompilation);
    }

    private Compilation getCompilationByIdIfExist(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Подборка с with id='%s' не найдена", compilationId)));
    }

    private void checkExistCompilationById(Long compilationId) {
        if (compilationRepository.countById(compilationId) == 0) {
            throw new NoFoundObjectException(String.format("Подборка с with id='%s' не найдена", compilationId));
        }
    }
}
