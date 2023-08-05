package ru.practicum.controller.privateController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestDto;
import ru.practicum.serviceImpl.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@Component
@RequiredArgsConstructor
public class PrivateControllerRequests {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")//+
    public List<RequestDto> getRequest(@PathVariable(name = "userId") Long userId) {
        return requestService.getAllRequestsByUserId(userId);
    }

    @PostMapping("/{userId}/requests")//+
    public RequestDto addRequest(@PathVariable Long userId,
                                 @RequestParam Long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")//++
    public RequestDto updateRequestCancel(@PathVariable(name = "userId") Long userId,
                                          @PathVariable(name = "requestId") Long requestId) {
        return requestService.cancelledRequestById(userId, requestId);
    }
}
