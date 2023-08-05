package ru.practicum.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.UpdateEventUserRequest;
import ru.practicum.exception.EventConflictException;
import ru.practicum.exception.IncorrectRequestException;
import ru.practicum.exception.NoFoundObjectException;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.enumModel.EventState;
import ru.practicum.repository.events.EventsRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.model.mapper.EventMapper.*;

@Service
@RequiredArgsConstructor
public class UserServiceEvent {
    private static final Integer HOURS_BEFORE_START_EVENT = 2;
    private final EventsRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Transactional
    public EventFullDto addEventsThisUser(NewEventDto request, Long userId) {
        checkTimeBeforeEventStart(request.getEventDate());

        User user = userService.getUserByIdIfExist(userId);
        Category category = categoryService.getCategoryByIdIfExist(request.getCategory());

        Event event = makeEvent(request, category, user);
        Event savedEvent = eventRepository.save(event);

        return makeEventFullDto(savedEvent);
    }

    public List<EventShortDto> getEventsThisUser(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        userService.checkExistUserById(userId);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        return makeEventShortDtoList(events);
    }

    public EventFullDto getEventsThisUserFull(Long userId, Long eventId) {
        userService.checkExistUserById(userId);
        Event event = getEventByIdAndInitiatorIdIfExist(eventId, userId);
        return makeEventFullDto(event);
    }

    public EventFullDto updateEventsThisUserFull(Long userId, Long eventId, UpdateEventUserRequest request) {
        userService.checkExistUserById(userId);

        Event foundEvent = getEventByIdAndInitiatorIdIfExist(eventId, userId);

        if (!Objects.equals(userId, foundEvent.getInitiator().getId())) {
            throw new EventConflictException(String.format("Пользователь с id='%s' не автор события с id='%s'",
                    userId, eventId));
        }

        if (Objects.equals(EventState.PUBLISHED, foundEvent.getState())) {
            throw new EventConflictException("Статус события должен быть 'PENDING' or 'CANCELED'");
        }

        if (Objects.nonNull(request.getTitle())) {
            foundEvent.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getEventDate())) {
            checkTimeBeforeEventStart(request.getEventDate());
            foundEvent.setEventDate(request.getEventDate());
        }
        if (Objects.nonNull(request.getAnnotation()) && StringUtils.hasLength(request.getAnnotation())) {
            foundEvent.setAnnotation(request.getAnnotation());
        }
        if (Objects.nonNull(request.getCategory())) {
            Category category = categoryService.getCategoryByIdIfExist(request.getCategory());
            foundEvent.setCategory(category);
        }
        if (Objects.nonNull(request.getDescription())) {
            foundEvent.setDescription(request.getDescription());
        }
        if (Objects.nonNull(request.getLocation())) {
            foundEvent.setLocation(request.getLocation());
        }
        if (Objects.nonNull(request.getParticipantLimit())) {
            foundEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (Objects.nonNull(request.getRequestModeration())) {
            foundEvent.setRequestModeration(request.getRequestModeration());
        }
        if (Objects.nonNull(request.getStateAction())) {
            switch (request.getStateAction()) {
                case SEND_TO_REVIEW:
                    foundEvent.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    foundEvent.setState(EventState.CANCELED);
                    break;
            }
        }

        Event updatedEvent = eventRepository.save(foundEvent);
        return makeEventFullDto(updatedEvent);
    }

    public Event getEventByIdAndInitiatorIdIfExist(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Событие с id='%s' и инициатором с id='%s' не найдено",
                        eventId, userId)));
    }

    private void checkTimeBeforeEventStart(LocalDateTime startDate) {
        LocalDateTime munTimePeriod = LocalDateTime.now().plusHours(HOURS_BEFORE_START_EVENT);
        if (startDate.isBefore(munTimePeriod)) {
            throw new IncorrectRequestException("Событие начинается менее чем через " + HOURS_BEFORE_START_EVENT +
                    " часов");
        }
    }
}
