package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.exception.ConflictException;
import com.github.not.n0w.islab1.exception.NotFoundException;
import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.dto.DisciplineRequestDto;
import com.github.not.n0w.islab1.repository.DisciplineRepository;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.service.DisciplineService;
import com.github.not.n0w.islab1.service.LabWorkService;
import com.github.not.n0w.islab1.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineServiceImpl implements DisciplineService {
    private final DisciplineRepository disciplineRepository;
    private final LabWorkRepository labWorkRepository;

    @Override
    @Transactional
    public void createDiscipline(DisciplineRequestDto dto) {
        Discipline discipline = Discipline.builder()
                .name(dto.getName())
                .practiceHours(dto.getPracticeHours())
                .build();
        disciplineRepository.save(discipline);
    }

    @Override
    public Discipline getDisciplineById(Integer id) {
        return disciplineRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Discipline with id=" + id + " not found"));
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        return (List<Discipline>) disciplineRepository.findAll();
    }

    @Override
    public List<LabWork> getRelatedLabWorks(Integer id) {
        return labWorkRepository.findAllByDiscipline_Id(id);
    }

    @Override
    @Transactional
    public void deleteDiscipline(Integer id) {
        Discipline discipline = getDisciplineById(id);
        if (labWorkRepository.existsLabWorkByDiscipline_Id(id)) {
            throw new ConflictException("Discipline with id=" + id + " is used by lab works. Reassign them first");
        }
        disciplineRepository.delete(discipline);
    }

    @Override
    @Transactional
    public void updateDiscipline(DisciplineRequestDto dto, Integer id) {
        Discipline discipline = getDisciplineById(id);
        discipline.setName(dto.getName());
        discipline.setPracticeHours(dto.getPracticeHours());
    }
}