package ru.practicum.service;

import ru.practicum.dto.HitsDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.model.Hits;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServer {

    List<StatsDto> getStats(LocalDateTime start,
                            LocalDateTime end,
                            List<String> uri,
                            Boolean unique) throws BadRequestException;

    Hits saveRequest(HitsDto hitsDto);
}
