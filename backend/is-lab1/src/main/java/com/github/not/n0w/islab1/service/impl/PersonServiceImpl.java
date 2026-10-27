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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final LabWorkRepository labWorkRepository;
    private final LocationService locationService;

    @Override
    @Transactional
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

        boolean isLocationRetained = isLocationRetained(id, person.getLocation().getId());

        personRepository.delete(person);
        if (isDeleteStrategy && isLocationRetained) {
            locationService.deleteLocation(person.getLocation().getId());
        }
    }

    @Override
    @Transactional
    public void updatePerson(PersonRequestDto person, Integer id, boolean isDeleteStrategy) {
        Person personEntity = getPersonById(id);
        Location oldLocation = personEntity.getLocation();

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