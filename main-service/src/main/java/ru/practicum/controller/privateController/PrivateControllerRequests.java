package ru.practicum.controller.privateController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestDto;
import ru.practicum.serviceImpl.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateControllerRequests {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getRequest(@PathVariable(name = "userId") Long userId) {
        return requestService.getAllRequestsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable Long userId,
                                 @RequestParam Long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto updateRequestCancel(@PathVariable(name = "userId") Long userId,
                                          @PathVariable(name = "requestId") Long requestId) {
        return requestService.cancelledRequestById(userId, requestId);
    }
}
