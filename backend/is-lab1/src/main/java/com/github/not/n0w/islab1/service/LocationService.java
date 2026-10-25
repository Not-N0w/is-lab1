package com.github.not.n0w.islab1.service;

import com.github.not.n0w.islab1.model.core.Location;
import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.LocationRequestDto;

import java.util.List;

public interface LocationService {
    void createLocation(LocationRequestDto location);
    void deleteLocation(Integer id);
    void updateLocation(LocationRequestDto location, Integer id);

    List<Person> getRelatedPersons(Integer id);

    List<Location> getAllLocations();
    Location getLocationById(Integer id);
}
