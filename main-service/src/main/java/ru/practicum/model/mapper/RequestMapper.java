package ru.practicum.model.mapper;

import ru.practicum.dto.RequestDto;
import ru.practicum.model.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static Request makeRequest(RequestDto requestDto) {
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setCreated(requestDto.getCreated());
        request.setStatus(requestDto.getStatus());
        return request;
    }

    public static RequestDto makeRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

    public static List<RequestDto> makeRequestDtoList(List<Request> requests) {
        List<RequestDto> requestDtoList = new ArrayList<>();
        for (Request request : requests)
            requestDtoList.add(makeRequestDto(request));
        return requestDtoList;
    }

    public static List<Request> makeRequestList(List<RequestDto> requests) {
        List<Request> requestList = new ArrayList<>();
        for (RequestDto request : requests)
            requestList.add(makeRequest(request));
        return requestList;
    }
}
