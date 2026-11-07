package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.exception.ConflictException;
import com.github.not.n0w.islab1.exception.NotFoundException;
import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.core.Location;
import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.PersonRequestDto;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.repository.PersonRepository;
import com.github.not.n0w.islab1.service.LocationService;
import com.github.not.n0w.islab1.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final LabWorkRepository labWorkRepository;
    private final LocationService locationService;

    private Location resolveLocation(PersonRequestDto dto) {
        Location location = dto.getLocation();
        if (location == null) {
            return locationService.getLocationById(dto.getLocationId());
        }
        if (location.getId() != null) {
            return locationService.getLocationById(location.getId());
        }
        return location;
    }

    @Override
    @Transactional
    public void createPerson(PersonRequestDto personDto) {
        Person person = Person.builder()
                .name(personDto.getName())
                .eyeColor(personDto.getEyeColor())
                .hairColor(personDto.getHairColor())
                .nationality(personDto.getNationality())
                .height(personDto.getHeight())
                .location(resolveLocation(personDto))
                .build();

        personRepository.save(person);
    }

    @Override
    public List<Person> getAllPersons() {
        return new ArrayList<>((List<Person>) personRepository.findAll());
    }

    @Override
    public Person getPersonById(Integer id) {
        return personRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Person with id=" + id + " not found")
        );
    }

    @Override
    public List<Person> getPersonsByLocationId(Integer id) {
        return personRepository.getPersonsByLocationId(id);
    }

    private boolean isLocationRetained(Integer personId, Integer locationId) {
        List<Person> persons = getPersonsByLocationId(locationId);
        return persons.size() == 1 && Objects.equals(persons.get(0).getId(), personId);
    }

    @Override
    @Transactional
    public void deletePerson(Integer id, boolean isDeleteStrategy) {
        Person person = getPersonById(id);
        if (!labWorkRepository.findAllByAuthor_Id(id).isEmpty()) {
            throw new ConflictException("Person with id=" + id + " is an author of lab works. Reassign them first");
        }

        Integer locationId = person.getLocation().getId();
        boolean isLocationRetained = isLocationRetained(id, locationId);

        personRepository.delete(person);
        if (isDeleteStrategy && isLocationRetained) {
            locationService.deleteLocation(locationId);
        }
    }

    @Override
    @Transactional
    public void updatePerson(PersonRequestDto person, Integer id, boolean isDeleteStrategy) {
        Person personEntity = getPersonById(id);
        Location oldLocation = personEntity.getLocation();
        Location location = resolveLocation(person);

        personEntity.setLocation(location);
        personEntity.setName(person.getName());
        personEntity.setEyeColor(person.getEyeColor());
        personEntity.setHairColor(person.getHairColor());
        personEntity.setNationality(person.getNationality());
        personEntity.setHeight(person.getHeight());
        personRepository.saveAndFlush(personEntity);

        boolean isAnotherLocation = !Objects.equals(oldLocation.getId(), location.getId());
        if (isDeleteStrategy && isAnotherLocation && getPersonsByLocationId(oldLocation.getId()).isEmpty()) {
            locationService.deleteLocation(oldLocation.getId());
        }
    }

    @Override
    public List<LabWork> getRelatedLabWorks(Integer id) {
        return labWorkRepository.findAllByAuthor_Id(id);
    }
}