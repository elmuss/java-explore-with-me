package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.location.LocationDto;
import org.example.model.Location;

@UtilityClass
public class LocationMapper {
    public static LocationDto modelToDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
