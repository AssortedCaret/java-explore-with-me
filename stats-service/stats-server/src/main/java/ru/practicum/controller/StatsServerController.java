package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitsDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.Hits;
import ru.practicum.service.StatServer;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
@Component
@RequiredArgsConstructor
public class StatsServerController {
    private final StatServer statServer;
    private final String DATE_FORM = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORM) LocalDateTime start,
                                   @RequestParam @DateTimeFormat(pattern = DATE_FORM) LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {

        log.info("Получен @GetMapping(/stats) с start= {}, end= {}, uris= {}, unique= {}", start, end, uris, unique);
        return statServer.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Hits saveRequest(@RequestBody HitsDto hitsDto) {
        log.info("Был отправлен запрос: @PostMapping(/hit)");
        return statServer.saveRequest(hitsDto);
    }
}
