package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.model.*;
import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.LabWorkRequestDto;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.service.CoordinatesService;
import com.github.not.n0w.islab1.service.DisciplineService;
import com.github.not.n0w.islab1.service.LabWorkService;
import com.github.not.n0w.islab1.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabWorkServiceImpl implements LabWorkService {
    private final LabWorkRepository labWorkRepository;

    private final PersonService personService;
    private final DisciplineService disciplineService;
    private final CoordinatesService coordinatesService;

    private LabWork mapLabWorkRequestDtoToLabWork(LabWorkRequestDto labWorkDto) {
        Person author = labWorkDto.isNewAuthor()
                ? labWorkDto.getAuthor()
                : personService.getPersonById(labWorkDto.getAuthorId());

        Discipline discipline = labWorkDto.isNewDiscipline()
                ? labWorkDto.getDiscipline()
                : disciplineService.getDisciplineById(labWorkDto.getDisciplineId());

        Coordinates coordinates = labWorkDto.isNewCoordinates()
                ? labWorkDto.getCoordinates()
                : coordinatesService.getCoordinatesById(labWorkDto.getCoordinatesId());


        return LabWork.builder()
                .tunedInWorks(labWorkDto.getTunedInWorks())
                .difficulty(labWorkDto.getDifficulty())
                .name(labWorkDto.getName())
                .description(labWorkDto.getDescription())
                .coordinates(coordinates)
                .discipline(discipline)
                .minimalPoint(labWorkDto.getMinimalPoint())
                .author(author)
                .build();
    }

    @Transactional
    @Override
    public void createLabWork(LabWorkRequestDto labWorkDto) {
        LabWork labWork = mapLabWorkRequestDtoToLabWork(labWorkDto);
        labWorkRepository.save(labWork);
    }

    private boolean isCoordinatesRetained(Integer labWorkId, Integer coordinatesId) {
        List<LabWork> labs = coordinatesService.getRelatedLabWorks(coordinatesId);
        return labs.size() == 1 && labs.get(0).getId().equals(labWorkId);
    }

    private boolean isDisciplineRetained(Integer labWorkId, Integer disciplineId) {
        List<LabWork> labs = disciplineService.getRelatedLabWorks(disciplineId);
        return labs.size() == 1 && labs.get(0).getId().equals(labWorkId);
    }

    private boolean isAuthorRetained(Integer labWorkId, Integer authorId) {
        List<LabWork> labs = personService.getRelatedLabWorks(authorId);
        return labs.size() == 1 && labs.get(0).getId().equals(labWorkId);
    }

    @Override
    @Transactional
    public void deleteLabWork(Integer id, boolean isDeleteStrategy) {
        if(isDeleteStrategy) {
            LabWork labWork = labWorkRepository.findById(id).orElse(null);
            if(labWork != null) {
                if(isCoordinatesRetained(id, labWork.getCoordinates().getId())) {
                    coordinatesService.deleteCoordinates(labWork.getCoordinates().getId());
                }
                if(isDisciplineRetained(id, labWork.getDiscipline().getId())) {
                    disciplineService.deleteDiscipline(labWork.getDiscipline().getId());
                }
                if(isAuthorRetained(id, labWork.getAuthor().getId())) {
                    personService.deletePerson(labWork.getAuthor().getId(), isDeleteStrategy);
                }
            }
        }
        labWorkRepository.deleteById(id);
    }

    @Override
    public void updateLabWork(LabWorkRequestDto labWorkDto, Integer id, boolean isDeleteStrategy) {
        if(isDeleteStrategy) {
            LabWork labWork = labWorkRepository.findById(id).orElse(null);
            if(labWork != null) {
                boolean isAnotherCoordinates = !labWork.getCoordinates().getId().equals(labWorkDto.getCoordinatesId());
                boolean isAnotherDiscipline = !labWork.getDiscipline().getId().equals(labWorkDto.getDisciplineId());
                boolean isAnotherAuthor = !labWork.getAuthor().getId().equals(labWorkDto.getAuthorId());

                if(isAnotherCoordinates && isCoordinatesRetained(id, labWork.getCoordinates().getId())) {
                    coordinatesService.deleteCoordinates(labWork.getCoordinates().getId());
                }
                if(isAnotherDiscipline && isDisciplineRetained(id, labWork.getDiscipline().getId())) {
                    disciplineService.deleteDiscipline(labWork.getDiscipline().getId());
                }
                if(isAnotherAuthor && isAuthorRetained(id, labWork.getAuthor().getId())) {
                    personService.deletePerson(labWork.getAuthor().getId(), true);
                }
            }
        }

        LabWork labWork = mapLabWorkRequestDtoToLabWork(labWorkDto);
        labWork.setId(id);
        labWorkRepository.save(labWork);
    }

    @Override
    public LabWork getLabWorkById(Integer id) {
        return labWorkRepository.findById(id).orElse(null);
    }

    @Override
    public List<LabWork> getAllLabWorks() {
        return (List<LabWork>) labWorkRepository.findAll();
    }

    @Override
    public void addLabToDiscipline(Integer labId, Integer disciplineId) {
        labWorkRepository.addLabToDiscipline(labId, disciplineId);
    }

    @Override
    public void addTopLabsToDiscipline(Integer disciplineId) {
        labWorkRepository.addTopLabsToDiscipline(disciplineId);
    }

    @Override
    public Double getSumOfMinimalPoints() {
        return labWorkRepository.getMinimalPointSum();
    }

    @Override
    public List<MinimalPointGroup> countByMinimalPoint() {
        return labWorkRepository.countByMinimalPoint();
    }

    @Override
    public List<Integer> getUniqueAuthors() {
        return labWorkRepository.getUniqueAuthorIds();
    }

    @Override
    public List<LabWork> getAllByAuthorId(Integer id) {
        return labWorkRepository.findAllByAuthor_Id(id);
    }
    @Override
    public List<LabWork> getAllByDisciplineId(Integer id) {
        return labWorkRepository.findAllByDiscipline_Id(id);
    }
    @Override
    public List<LabWork> getAllByCoordinatesId(Integer id) {
        return labWorkRepository.findAllByCoordinates_Id(id);
    }
}
