package ru.practicum.controller.publicController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.serviceImpl.PublicServiceEvent;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicControllerEvents {
    private final PublicServiceEvent eventService;

    @GetMapping
    public List<EventShortDto> getAllEvents(@RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "categories", required = false) List<Long> categories,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                            @RequestParam(name = "sort", defaultValue = "event_date") String sort,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                            HttpServletRequest request) {
        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventsId(@PathVariable(name = "eventId") @Positive Long eventId,
                                    HttpServletRequest request) {
        return eventService.getEventsId(eventId, request.getRemoteAddr(), request.getRequestURI());
    }
}
