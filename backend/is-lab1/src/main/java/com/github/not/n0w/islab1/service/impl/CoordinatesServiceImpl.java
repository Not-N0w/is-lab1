package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.dto.CoordinatesRequestDto;
import com.github.not.n0w.islab1.repository.CoordinatesRepository;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.service.CoordinatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoordinatesServiceImpl implements CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;
    private final LabWorkRepository labWorkRepository;

    @Override
    public void createCoordinates(CoordinatesRequestDto coordinatesDto) {
        Coordinates coordinates = Coordinates.builder()
                .x(coordinatesDto.getX())
                .y(coordinatesDto.getY())
                .build();

        coordinatesRepository.save(coordinates);
    }

    @Override
    public Coordinates getCoordinatesById(Integer id) {
        return coordinatesRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Coordinates with id=" + id + " not found")
        );
    }

    @Override
    public List<Coordinates> getAllCoordinates() {
        return (List<Coordinates>) coordinatesRepository.findAll();
    }

    @Override
    public List<LabWork> getRelatedLabWorks(Integer id) {
        return labWorkRepository.findAllByCoordinates_Id(id);
    }

    @Override
    public void deleteCoordinates(Integer id) {
        try {
            coordinatesRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Cannot delete coordinates with id: {}", id);
            log.error("Error message: {}", e.getMessage());
        }
    }

    @Override
    public void updateCoordinates(CoordinatesRequestDto coordinates, Integer id) {
        Coordinates coordinatesEntity = getCoordinatesById(id);
        coordinatesEntity.setX(coordinates.getX());
        coordinatesEntity.setY(coordinates.getY());
        coordinatesRepository.save(coordinatesEntity);
    }
}
