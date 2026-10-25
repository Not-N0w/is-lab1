package com.github.not.n0w.islab1.service.impl;

import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.core.Location;
import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.PersonRequestDto;
import com.github.not.n0w.islab1.repository.LabWorkRepository;
import com.github.not.n0w.islab1.repository.PersonRepository;
import com.github.not.n0w.islab1.service.LabWorkService;
import com.github.not.n0w.islab1.service.LocationService;
import com.github.not.n0w.islab1.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final LabWorkRepository labWorkRepository;

    private final LocationService locationService;

    @Override
    public void createPerson(PersonRequestDto personDto) {
        Location location = personDto.isNewLocation()
                ? personDto.getLocation()
                : locationService.getLocationById(personDto.getLocationId());

        Person person = Person.builder()
                .name(personDto.getName())
                .eyeColor(personDto.getEyeColor())
                .hairColor(personDto.getHairColor())
                .nationality(personDto.getNationality())
                .height(personDto.getHeight())
                .location(location)
                .build();

        personRepository.save(person);
    }
    @Override
    public List<Person> getAllPersons() {
        return (List<Person>) personRepository.findAll();
    }

    @Override
    public Person getPersonById(Integer id) {
        return personRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Person with id=" + id + " not found")
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
        boolean isLocationRetained = isLocationRetained(id, person.getLocation().getId());
        if (isDeleteStrategy && isLocationRetained) {
            locationService.deleteLocation(person.getLocation().getId());
        }

        try {
            personRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Cannot delete person with id: {}", id);
            log.error("Error message: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updatePerson(PersonRequestDto person, Integer id, boolean isDeleteStrategy) {
        Person personEntity = getPersonById(id);

        boolean isAnotherLocation = !Objects.equals(personEntity.getLocation().getId(), person.getLocationId());

        if(isAnotherLocation && isDeleteStrategy && isLocationRetained(id, person.getLocationId())) {
            locationService.deleteLocation(personEntity.getLocation().getId());
        }

        Location location = person.isNewLocation()
                ? person.getLocation()
                : locationService.getLocationById(person.getLocationId());

        personEntity.setLocation(location);
        personEntity.setName(person.getName());
        personEntity.setEyeColor(person.getEyeColor());
        personEntity.setHairColor(person.getHairColor());
        personEntity.setNationality(person.getNationality());
        personEntity.setHeight(person.getHeight());
        personRepository.save(personEntity);
    }

    @Override
    public List<LabWork> getRelatedLabWorks(Integer id) {
        return labWorkRepository.findAllByAuthor_Id(id);
    }
}
