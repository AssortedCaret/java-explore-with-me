package ru.practicum.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.HitsDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.exception.IncorrectRequestException;
import ru.practicum.exception.NoFoundObjectException;
import ru.practicum.model.Event;
import ru.practicum.model.enumModel.EventState;
import ru.practicum.repository.events.EventsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.model.mapper.EventMapper.makeEventFullDto;
import static ru.practicum.model.mapper.EventMapper.makeEventShortDtoList;

@Service
@RequiredArgsConstructor
public class PublicServiceEvent {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventsRepository eventRepository;
    private final StatsClient statClient;

    public List<EventShortDto> getAllEvents(String text, List<Long> categories, Boolean paid,
                                            LocalDateTime startDate, LocalDateTime endDate,
                                            Boolean onlyAvailable, String sort,
                                            Integer from, Integer size, String ip, String uri) {
        checkEndIsAfterStart(startDate, endDate);
        saveInfoToStatistics(ip, uri);

        Pageable pageable = PageRequest.of(from / size, size);
        Specification<Event> specification = Specification.where(null);

        if (Objects.nonNull(text)) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                            "%" + text.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                            "%" + text.toLowerCase() + "%")
            ));
        }

        if (Objects.nonNull(categories) && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }

        LocalDateTime startDateTime = Objects.requireNonNullElse(startDate, LocalDateTime.now());

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));

        if (Objects.nonNull(endDate)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("eventDate"), endDate));
        }

        if (Objects.nonNull(onlyAvailable) && onlyAvailable) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

        List<Event> events = eventRepository.findAll(specification, pageable);
        updateViewsOfEvents(events);

        return makeEventShortDtoList(events);
    }

    public EventFullDto getEventsId(Long eventId, String ip, String uri) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NoFoundObjectException("Event not found"));

        saveInfoToStatistics(ip, uri);
        updateViewsOfEvents(List.of(event));

        return makeEventFullDto(event);
    }

    public List<Event> getAllEventsByIdIn(Set<Long> events) {
        return eventRepository.findAllByIdIn(events);
    }

    private void checkEndIsAfterStart(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IncorrectRequestException("Неверная дата");
        }
    }

    private void saveInfoToStatistics(String ip, String uri) {
        statClient.saveInfo(HitsDto.builder()
                .app("ewm-main-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build());
    }

    private void updateViewsOfEvents(List<Event> events) {
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .collect(Collectors.toList());

        List<StatsDto> statistics = getViewsStatistics(uris);

        events.forEach(event -> {
            StatsDto foundViewInStats = statistics.stream()
                    .filter(statDto -> {
                        Long eventIdFromStats = Long.parseLong(statDto.getUri().substring("/events/".length()));
                        return Objects.equals(eventIdFromStats, event.getId());
                    })
                    .findFirst()
                    .orElse(null);

            long currentCountViews = foundViewInStats == null ? 0 : foundViewInStats.getHits();
            event.setViews(currentCountViews + 1);
        });

        eventRepository.saveAll(events);
    }

    private List<StatsDto> getViewsStatistics(List<String> uris) {
        return statClient.getStatistics(
                LocalDateTime.now().minusYears(100).format(DATE_TIME_FORMATTER),
                LocalDateTime.now().plusYears(5).format(DATE_TIME_FORMATTER),
                uris,
                true);
    }
}
