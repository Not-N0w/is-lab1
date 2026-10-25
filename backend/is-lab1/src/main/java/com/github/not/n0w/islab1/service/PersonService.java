package com.github.not.n0w.islab1.service;

import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.PersonRequestDto;

import java.util.List;

public interface PersonService {
    void createPerson(PersonRequestDto person);
    void deletePerson(Integer id, boolean isDeleteStrategy);
    void updatePerson(PersonRequestDto person, Integer id, boolean isDeleteStrategy);

    List<LabWork> getRelatedLabWorks(Integer id);

    List<Person> getAllPersons();
    Person getPersonById(Integer id);

    List<Person> getPersonsByLocationId(Integer id);
}
