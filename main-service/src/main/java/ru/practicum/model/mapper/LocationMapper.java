package ru.practicum.model.mapper;

import ru.practicum.dto.LocationDto;
import ru.practicum.model.Location;

public class LocationMapper {

    public static LocationDto makeLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }

    public static Location makeLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }
}
