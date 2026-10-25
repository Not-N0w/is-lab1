package com.github.not.n0w.islab1.service;

import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.dto.CoordinatesRequestDto;

import java.util.List;

public interface CoordinatesService {
    void createCoordinates(CoordinatesRequestDto coordinates);
    void deleteCoordinates(Integer id);
    void updateCoordinates(CoordinatesRequestDto coordinates, Integer id);

    List<LabWork> getRelatedLabWorks(Integer id);

    List<Coordinates> getAllCoordinates();
    Coordinates getCoordinatesById(Integer id);
}
