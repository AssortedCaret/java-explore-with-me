package ru.practicum.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.RequestDto;
import ru.practicum.exception.EventConflictException;
import ru.practicum.exception.IncorrectRequestException;
import ru.practicum.exception.NoFoundObjectException;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.model.enumModel.EventState;
import ru.practicum.model.enumModel.RequestStatus;
import ru.practicum.model.mapper.RequestMapper;
import ru.practicum.repository.events.EventsRepository;
import ru.practicum.repository.request.RequestRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.model.mapper.RequestMapper.makeRequestDto;
import static ru.practicum.model.mapper.RequestMapper.makeRequestDtoList;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventsRepository eventRepository;

    @Transactional
    public RequestDto addRequest(Long userId, Long eventId) {
        User user = userService.getUserByIdIfExist(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Событие с id='%s' не найдено", eventId)));

        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new EventConflictException(String.format("Пользователь с id='%s' - автор события с id='%s' " +
                    "и не может создать запрос", userId, eventId));
        }

        if (!requestRepository.findByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new EventConflictException("Нельзя добавить повторный запрос на участие в событии");
        }

        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new EventConflictException("Нельзя добавить запрос на участие в неопубликованном событии");
        }

        Request request = new Request(null, LocalDateTime.now(), event, user, RequestStatus.PENDING);

        Long confirmed = event.getConfirmedRequests();
        Long limit = event.getParticipantLimit();

        if (limit == 0) {
            event.setConfirmedRequests(confirmed + 1);
            eventRepository.save(event);
            request.setStatus(RequestStatus.CONFIRMED);
        } else if (confirmed < limit) {
            if (!event.getRequestModeration()) {
                event.setConfirmedRequests(confirmed + 1);
                eventRepository.save(event);
                request.setStatus(RequestStatus.PENDING);
            }
        } else {
            throw new EventConflictException(String.format("Свободных мест для записи на события с id='%s' нет", eventId));
        }

        Request savedRequest = requestRepository.save(request);
        return makeRequestDto(savedRequest);
    }

    public List<RequestDto> getAllRequestsByUserId(Long userId) {
        userService.checkExistUserById(userId);

        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return makeRequestDtoList(requests);
    }

    @Transactional
    public RequestDto cancelledRequestById(Long userId, Long requestId) {
        userService.checkExistUserById(userId);

        Request request = requestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(() ->
                new NoFoundObjectException(String.format("Запрос с id='%s' и реквестором с id='%s' не найден",
                        requestId, userId)));

        if ((Objects.equals(request.getStatus(), RequestStatus.CANCELED))
                || (Objects.equals(request.getStatus(), RequestStatus.REJECTED))) {
            throw new IncorrectRequestException(String.format("Запрос с id='%s' и реквестором с id='%s' уже отменен",
                    requestId, userId));
        }

        request.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return makeRequestDto(savedRequest);
    }

    @Transactional
    public EventRequestStatusUpdateResult updateEventsThisUserFullRequests(Long userId, Long eventId,
                                                                           EventRequestStatusUpdateRequest request) {
        userService.checkExistUserById(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NoFoundObjectException(String.format(
                        "Запрос с id='%s' и инициатором с id='%s' не найден", eventId, userId)));


        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new EventConflictException("Достигнут лимит заявок на участие в событии");
        }

        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();

        List<Request> requests = requestRepository.findAllById(request.getRequestIds());
        for (Request item : requests) {
            if (Objects.equals(item.getStatus(), RequestStatus.PENDING)) {
                if (event.getParticipantLimit() == 0) {
                    item.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);

                } else if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                    if (!event.getRequestModeration() ||
                            (Objects.equals(request.getStatus(), RequestStatus.CONFIRMED))) {
                        item.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        confirmed.add(item);
                    } else {
                        item.setStatus(RequestStatus.REJECTED);
                        rejected.add(item);
                    }
                } else {
                    throw new EventConflictException("Статус можно изменить только у заявок в статусе WAITING");
                }

            }
        }
        eventRepository.save(event);

        return new EventRequestStatusUpdateResult(confirmed.stream()
                .map(RequestMapper::makeRequestDto)
                .collect(Collectors.toList()),
                rejected.stream()
                        .map(RequestMapper::makeRequestDto)
                        .collect(Collectors.toList()));
    }

    public List<RequestDto> getEventsThisUserFullRequests(Long userId, Long eventId) {
        userService.checkExistUserById(userId);

        if (!eventRepository.existsByInitiatorIdAndId(userId, eventId)) {
            throw new EventConflictException("Пользователь не инициатор события");
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return makeRequestDtoList(requests);
    }
}
