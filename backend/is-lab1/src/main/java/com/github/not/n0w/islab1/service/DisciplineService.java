package com.github.not.n0w.islab1.service;

import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.dto.DisciplineRequestDto;

import java.util.List;

public interface DisciplineService {
    void createDiscipline(DisciplineRequestDto discipline);
    void deleteDiscipline(Integer id);
    void updateDiscipline(DisciplineRequestDto discipline, Integer id);

    List<LabWork> getRelatedLabWorks(Integer id);

    List<Discipline> getAllDisciplines();
    Discipline getDisciplineById(Integer id);
}
