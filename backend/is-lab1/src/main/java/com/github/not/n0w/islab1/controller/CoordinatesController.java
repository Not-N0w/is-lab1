package com.github.not.n0w.islab1.controller;

import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.dto.CoordinatesRequestDto;
import com.github.not.n0w.islab1.service.CoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/coordinates")
@RequiredArgsConstructor
public class CoordinatesController {
    private final CoordinatesService coordinatesService;

    @GetMapping("/all")
    public List<Coordinates> getAllCoordinates() {
        return coordinatesService.getAllCoordinates();
    }

    @PostMapping("/create")
    public void createCoordinates(@RequestBody CoordinatesRequestDto coordinatesDto) {
        coordinatesService.createCoordinates(coordinatesDto);
    }

    @PatchMapping("/update/{id}")
    public void updateCoordinates(@RequestBody CoordinatesRequestDto coordinatesDto, @PathVariable("id") Integer id) {
        coordinatesService.updateCoordinates(coordinatesDto, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCoordinates(@PathVariable("id") Integer id) {
        coordinatesService.deleteCoordinates(id);
    }

    @GetMapping("/get/{id}")
    public Coordinates getCoordinatesById(@PathVariable("id") Integer id) {
        return coordinatesService.getCoordinatesById(id);
    }
}
