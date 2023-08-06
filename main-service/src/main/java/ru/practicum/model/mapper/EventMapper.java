package ru.practicum.model.mapper;

import ru.practicum.dto.*;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.model.enumModel.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    public static Event makeEvent(NewEventDto eventNewDto, Category category, User user) {
        Event event = new Event();
        Location location = new Location();
        location.setLat(eventNewDto.getLocation().getLat());
        location.setLon(eventNewDto.getLocation().getLon());
        event.setAnnotation(eventNewDto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(eventNewDto.getDescription());
        event.setEventDate(eventNewDto.getEventDate());
        event.setInitiator(user);
        event.setLocation(location);
        event.setPaid(eventNewDto.getPaid());
        event.setParticipantLimit(eventNewDto.getParticipantLimit());
        event.setRequestModeration(eventNewDto.getRequestModeration());
        event.setTitle(eventNewDto.getTitle());
        event.setState(EventState.PENDING);
        event.setPublishedOn(LocalDateTime.now());
        event.setViews(0L);
        return event;
    }

    public static EventShortDto makeEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        CategoryDto category = new CategoryDto();
        category.setId(event.getCategory().getId());
        category.setName(event.getCategory().getName());
        UserShortDto user = new UserShortDto();
        user.setId(event.getInitiator().getId());
        user.setName(event.getInitiator().getName());
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(category);
        eventShortDto.setConfirmedRequests(eventShortDto.getConfirmedRequests());
        eventShortDto.setEventDate(eventShortDto.getEventDate());
        eventShortDto.setInitiator(user);
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setViews(event.getViews());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        return eventShortDto;
    }

    public static EventFullDto makeEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        CategoryDto category = new CategoryDto();
        category.setId(event.getCategory().getId());
        category.setName(event.getCategory().getName());
        UserShortDto user = new UserShortDto();
        user.setId(event.getInitiator().getId());
        user.setName(event.getInitiator().getName());
        LocationDto location = new LocationDto();
        location.setLon(event.getLocation().getLon());
        location.setLat(event.getLocation().getLat());
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(category);
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(user);
        eventFullDto.setLocation(location);
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public static List<EventShortDto> makeEventShortDtoList(List<Event> events) {
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        for (Event event : events)
            eventShortDtoList.add(makeEventShortDto(event));
        return eventShortDtoList;
    }

    public static List<EventFullDto> makeEventFullDtoList(List<Event> events) {
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        for (Event event : events)
            eventFullDtoList.add(makeEventFullDto(event));
        return eventFullDtoList;
    }
}
