package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitsMapper;
import ru.practicum.dto.HitsDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Hits;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServerImpl implements StatServer {
    private final StatRepository statRepository;

    @Override
    public List<StatsDto> getStats(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uri,
                                   Boolean unique) throws BadRequestException {
        if (start.isAfter(end))
            throw new BadRequestException("Неверная дата");
        if (uri == null || uri.isEmpty()) {
            return statRepository.getStatsHitWithoutURI(start, end);
        } else if (unique == true) {
            return statRepository.getStatsHitUnique(start, end, uri);
        } else {
            return statRepository.getStatsHitNotUnique(start, end, uri);
        }
    }

    @Override
    public Hits saveRequest(HitsDto hitsDto) {
        Hits newHits = HitsMapper.makeHits(hitsDto);
        statRepository.save(newHits);
        return newHits;
    }
}
