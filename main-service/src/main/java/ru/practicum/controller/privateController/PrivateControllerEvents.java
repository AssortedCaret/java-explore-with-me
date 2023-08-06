package ru.practicum.controller.privateController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.serviceImpl.RequestService;
import ru.practicum.serviceImpl.UserServiceEvent;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateControllerEvents {
    private final UserServiceEvent eventService;
    private final RequestService requestService;

    @GetMapping("/{userId}/events")//+
    public List<EventShortDto> getEventsThisUser(@PathVariable(name = "userId") @Positive Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return eventService.getEventsThisUser(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")//+
    public EventFullDto getEventsThisUserFull(@PathVariable(name = "userId") @Positive Long userId,
                                              @PathVariable(name = "eventId") @Positive Long eventId) {
        return eventService.getEventsThisUserFull(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")//+
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getEventsThisUserFullRequests(@PathVariable(name = "userId") Long userId,
                                                          @PathVariable(name = "eventId") Long eventId) {
        return requestService.getEventsThisUserFullRequests(userId, eventId);
    }

    @PostMapping("/{userId}/events")//+
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEventsThisUser(@PathVariable(name = "userId") @Positive Long userId,
                                          @RequestBody @Valid NewEventDto request) {
        return eventService.addEventsThisUser(request, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")//+
    public EventFullDto updateEventsThisUserFull(@PathVariable(name = "userId") @Positive Long userId,
                                                 @PathVariable(name = "eventId") @Positive Long eventId,
                                                 @RequestBody @Valid UpdateEventUserRequest request) {
        return eventService.updateEventsThisUserFull(userId, eventId, request);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")//+
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateEventsThisUserFullRequests(@PathVariable(name = "userId") Long userId,
                                                                           @PathVariable(name = "eventId") Long eventId,
                                                                           @RequestBody EventRequestStatusUpdateRequest request) {
        return requestService.updateEventsThisUserFullRequests(userId, eventId, request);
    }
}
