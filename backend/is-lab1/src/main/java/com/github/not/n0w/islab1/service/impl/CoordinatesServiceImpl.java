package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.exception.ConflictException;
import com.github.not.n0w.islab1.exception.NotFoundException;
import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.dto.CoordinatesRequestDto;
import com.github.not.n0w.islab1.repository.CoordinatesRepository;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.service.CoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatesServiceImpl implements CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;
    private final LabWorkRepository labWorkRepository;

    @Override
    @Transactional
    public void createCoordinates(CoordinatesRequestDto dto) {
        Coordinates coordinates = Coordinates.builder()
                .x(dto.getX())
                .y(dto.getY())
                .build();
        coordinatesRepository.save(coordinates);
    }

    @Override
    public Coordinates getCoordinatesById(Integer id) {
        return coordinatesRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Coordinates with id=" + id + " not found"));
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
    @Transactional
    public void deleteCoordinates(Integer id) {
        Coordinates coordinates = getCoordinatesById(id);
        if (labWorkRepository.existsLabWorkByCoordinates_Id(id)) {
            throw new ConflictException("Coordinates with id=" + id + " are used by lab works. Reassign them first");
        }
        coordinatesRepository.delete(coordinates);
    }

    @Override
    @Transactional
    public void updateCoordinates(CoordinatesRequestDto dto, Integer id) {
        Coordinates coordinates = getCoordinatesById(id);
        coordinates.setX(dto.getX());
        coordinates.setY(dto.getY());
    }
}