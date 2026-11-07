package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.exception.NotFoundException;
import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.core.Location;
import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.LabWorkRequestDto;
import com.github.not.n0w.islab1.model.ext.MinimalPointCount;
import com.github.not.n0w.islab1.model.ext.MinimalPointGroup;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LabWorkServiceImpl implements LabWorkService {
    private final LabWorkRepository labWorkRepository;
    private final PersonService personService;
    private final DisciplineService disciplineService;
    private final CoordinatesService coordinatesService;
    private final LocationService locationService;

    private Person resolveAuthor(LabWorkRequestDto dto) {
        Person author = dto.getAuthor();
        if (author == null) {
            return dto.getAuthorId() != null ? personService.getPersonById(dto.getAuthorId()) : null;
        }
        if (author.getId() != null) {
            return personService.getPersonById(author.getId());
        }
        Location location = author.getLocation();
        if (location != null && location.getId() != null) {
            author.setLocation(locationService.getLocationById(location.getId()));
        }
        return author;
    }

    private Discipline resolveDiscipline(LabWorkRequestDto dto) {
        Discipline discipline = dto.getDiscipline();
        if (discipline == null) {
            return disciplineService.getDisciplineById(dto.getDisciplineId());
        }
        if (discipline.getId() != null) {
            return disciplineService.getDisciplineById(discipline.getId());
        }
        return discipline;
    }

    private Coordinates resolveCoordinates(LabWorkRequestDto dto) {
        Coordinates coordinates = dto.getCoordinates();
        if (coordinates == null) {
            return coordinatesService.getCoordinatesById(dto.getCoordinatesId());
        }
        if (coordinates.getId() != null) {
            return coordinatesService.getCoordinatesById(coordinates.getId());
        }
        return coordinates;
    }

    private LabWork mapLabWorkRequestDtoToLabWork(LabWorkRequestDto labWorkDto) {
        return LabWork.builder()
                .tunedInWorks(labWorkDto.getTunedInWorks())
                .difficulty(labWorkDto.getDifficulty())
                .name(labWorkDto.getName())
                .description(labWorkDto.getDescription())
                .coordinates(resolveCoordinates(labWorkDto))
                .discipline(resolveDiscipline(labWorkDto))
                .minimalPoint(labWorkDto.getMinimalPoint())
                .author(resolveAuthor(labWorkDto))
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
        LabWork labWork = getLabWorkById(id);
        Coordinates coordinates = labWork.getCoordinates();
        Discipline discipline = labWork.getDiscipline();
        Person author = labWork.getAuthor();

        boolean deleteCoordinates = isDeleteStrategy && isCoordinatesRetained(id, coordinates.getId());
        boolean deleteDiscipline = isDeleteStrategy && isDisciplineRetained(id, discipline.getId());
        boolean deleteAuthor = isDeleteStrategy && author != null && isAuthorRetained(id, author.getId());

        labWorkRepository.delete(labWork);

        if (deleteCoordinates) {
            coordinatesService.deleteCoordinates(coordinates.getId());
        }
        if (deleteDiscipline) {
            disciplineService.deleteDiscipline(discipline.getId());
        }
        if (deleteAuthor) {
            personService.deletePerson(author.getId(), true);
        }
    }

    @Override
    @Transactional
    public void updateLabWork(LabWorkRequestDto dto, Integer id, boolean isDeleteStrategy) {
        LabWork labWork = getLabWorkById(id);
        Coordinates oldCoordinates = labWork.getCoordinates();
        Discipline oldDiscipline = labWork.getDiscipline();
        Person oldAuthor = labWork.getAuthor();

        LabWork updated = mapLabWorkRequestDtoToLabWork(dto);

        labWork.setName(updated.getName());
        labWork.setDescription(updated.getDescription());
        labWork.setDifficulty(updated.getDifficulty());
        labWork.setMinimalPoint(updated.getMinimalPoint());
        labWork.setTunedInWorks(updated.getTunedInWorks());
        labWork.setCoordinates(updated.getCoordinates());
        labWork.setDiscipline(updated.getDiscipline());
        labWork.setAuthor(updated.getAuthor());

        labWorkRepository.flush();

        if (isDeleteStrategy) {
            boolean isAnotherCoordinates = !Objects.equals(oldCoordinates.getId(), labWork.getCoordinates().getId());
            boolean isAnotherDiscipline = !Objects.equals(oldDiscipline.getId(), labWork.getDiscipline().getId());
            boolean isAnotherAuthor = oldAuthor != null
                    && (labWork.getAuthor() == null || !Objects.equals(oldAuthor.getId(), labWork.getAuthor().getId()));

            if (isAnotherCoordinates && coordinatesService.getRelatedLabWorks(oldCoordinates.getId()).isEmpty()) {
                coordinatesService.deleteCoordinates(oldCoordinates.getId());
            }
            if (isAnotherDiscipline && disciplineService.getRelatedLabWorks(oldDiscipline.getId()).isEmpty()) {
                disciplineService.deleteDiscipline(oldDiscipline.getId());
            }
            if (isAnotherAuthor && personService.getRelatedLabWorks(oldAuthor.getId()).isEmpty()) {
                personService.deletePerson(oldAuthor.getId(), true);
            }
        }
    }

    @Override
    public LabWork getLabWorkById(Integer id) {
        return labWorkRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LabWork with id=" + id + " not found"));
    }

    @Override
    public List<LabWork> getAllLabWorks() {
        return new ArrayList<>((List<LabWork>) labWorkRepository.findAll());
    }

    @Override
    @Transactional
    public void addLabToDiscipline(Integer labId, Integer disciplineId) {
        getLabWorkById(labId);
        disciplineService.getDisciplineById(disciplineId);
        labWorkRepository.addLabToDiscipline(labId, disciplineId);
    }

    @Override
    @Transactional
    public void addTopLabsToDiscipline(Integer disciplineId) {
        disciplineService.getDisciplineById(disciplineId);
        labWorkRepository.addTopLabsToDiscipline(disciplineId);
    }

    @Override
    public Double getSumOfMinimalPoints() {
        return labWorkRepository.getMinimalPointSum();
    }

    @Override
    public List<MinimalPointGroup> countByMinimalPoint() {
        List<MinimalPointGroup> result = new ArrayList<>();
        for (MinimalPointGroup g : labWorkRepository.countByMinimalPoint()) {
            result.add(new MinimalPointCount(g.getMinimalPoints(), g.getCount()));
        }
        return result;
    }

    @Override
    public List<Integer> getUniqueAuthors() {
        return new ArrayList<>(labWorkRepository.getUniqueAuthorIds());
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