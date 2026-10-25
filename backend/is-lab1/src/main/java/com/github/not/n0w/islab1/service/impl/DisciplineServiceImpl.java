package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.dto.DisciplineRequestDto;
import com.github.not.n0w.islab1.repository.DisciplineRepository;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.service.DisciplineService;
import com.github.not.n0w.islab1.service.LabWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisciplineServiceImpl implements DisciplineService {
    private final DisciplineRepository disciplineRepository;
    private final LabWorkRepository labWorkRepository;

    @Override
    public void createDiscipline(DisciplineRequestDto disciplineDto) {
        Discipline discipline = Discipline.builder()
                .name(disciplineDto.getName())
                .practiceHours(disciplineDto.getPracticeHours())
                .build();

        disciplineRepository.save(discipline);
    }

    @Override
    public Discipline getDisciplineById(Integer id) {
        return disciplineRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Discipline with id=" + id + " not found")
        );
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
    public void deleteDiscipline(Integer id) {
        try {
            disciplineRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Cannot delete discipline with id: {}", id);
            log.error("Error message: {}", e.getMessage());
        }
    }

    @Override
    public void updateDiscipline(DisciplineRequestDto discipline, Integer id) {
        Discipline disciplineEntity = getDisciplineById(id);
        disciplineEntity.setName(discipline.getName());
        disciplineEntity.setPracticeHours(discipline.getPracticeHours());
        disciplineRepository.save(disciplineEntity);
    }
}
