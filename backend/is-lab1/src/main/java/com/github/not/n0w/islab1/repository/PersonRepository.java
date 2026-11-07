package com.github.not.n0w.islab1.repository;

import com.github.not.n0w.islab1.model.core.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    List<Person> getPersonsByLocationId(Integer id);
}
