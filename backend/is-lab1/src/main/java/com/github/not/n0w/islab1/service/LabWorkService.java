package com.github.not.n0w.islab1.service;

import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.MinimalPointGroup;
import com.github.not.n0w.islab1.model.dto.LabWorkRequestDto;

import java.util.List;

public interface LabWorkService {
    void createLabWork(LabWorkRequestDto labWork);
    void deleteLabWork(Integer id, boolean isDeleteStrategy);
    void updateLabWork(LabWorkRequestDto labWork, Integer id, boolean isDeleteStrategy);

    LabWork getLabWorkById(Integer id);
    List<LabWork> getAllLabWorks();

    void addLabToDiscipline(Integer labId, Integer disciplineId);
    void addTopLabsToDiscipline(Integer disciplineId);
    Double getSumOfMinimalPoints();
    List<MinimalPointGroup> countByMinimalPoint();
    List<Integer> getUniqueAuthors();

    List<LabWork> getAllByAuthorId(Integer id);
    List<LabWork> getAllByDisciplineId(Integer id);
    List<LabWork> getAllByCoordinatesId(Integer id);
}
