package com.github.not.n0w.islab1.repository;

import com.github.not.n0w.islab1.model.core.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Integer> {
    List<Person> getPersonsByLocationId(Integer id);
}
