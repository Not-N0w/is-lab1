package com.github.not.n0w.islab1.controller;

import com.github.not.n0w.islab1.model.core.Location;
import com.github.not.n0w.islab1.model.dto.LocationRequestDto;
import com.github.not.n0w.islab1.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/all")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping("/create")
    public void createLocation(@RequestBody LocationRequestDto locationDto) {
        locationService.createLocation(locationDto);
    }

    @PatchMapping("/update/{id}")
    public void updateLocation(@RequestBody LocationRequestDto locationDto, @PathVariable("id") Integer id) {
        locationService.updateLocation(locationDto, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLocation(@PathVariable("id") Integer id) {
        locationService.deleteLocation(id);
    }

    @GetMapping("/get/{id}")
    public Location getLocationById(@PathVariable("id") Integer id) {
        return locationService.getLocationById(id);
    }
}
