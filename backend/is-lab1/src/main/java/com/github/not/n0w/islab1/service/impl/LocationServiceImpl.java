package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.exception.ConflictException;
import com.github.not.n0w.islab1.exception.NotFoundException;
import com.github.not.n0w.islab1.model.core.Location;
import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.LocationRequestDto;
import com.github.not.n0w.islab1.repository.LocationRepository;
import com.github.not.n0w.islab1.repository.PersonRepository;
import com.github.not.n0w.islab1.service.LocationService;
import com.github.not.n0w.islab1.service.PersonService;
import com.github.not.n0w.islab1.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;

    @Override
    public Location getLocationById(Integer id) {
        return locationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Location with id=" + id + " not found")
        );
    }

    @Override
    @Transactional
    public void createLocation(LocationRequestDto locationDto) {
        Location location = Location.builder()
                .x(locationDto.getX())
                .y(locationDto.getY())
                .z(locationDto.getZ())
                .name(locationDto.getName())
                .build();

        locationRepository.save(location);
    }

    @Override
    public List<Location> getAllLocations() {
        return (List<Location>) locationRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteLocation(Integer id) {
        Location location = getLocationById(id);
        if (!personRepository.getPersonsByLocationId(id).isEmpty()) {
            throw new ConflictException("Location with id=" + id + " is used by persons. Reassign them first");
        }
        locationRepository.delete(location);
    }

    @Override
    @Transactional
    public void updateLocation(LocationRequestDto location, Integer id) {
        Location locationEntity = getLocationById(id);
        locationEntity.setX(location.getX());
        locationEntity.setY(location.getY());
        locationEntity.setZ(location.getZ());
        locationEntity.setName(location.getName());
        locationRepository.save(locationEntity);
    }

    @Override
    public List<Person> getRelatedPersons(Integer id) {
        return personRepository.getPersonsByLocationId(id);
    }
}