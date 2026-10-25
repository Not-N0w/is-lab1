package com.github.not.n0w.islab1.controller;

import com.github.not.n0w.islab1.model.core.Person;
import com.github.not.n0w.islab1.model.dto.PersonRequestDto;
import com.github.not.n0w.islab1.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/all")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @PostMapping("/create")
    public void addPerson(PersonRequestDto personRequestDto) {
        personService.createPerson(personRequestDto);
    }

    @PatchMapping("/update/{id}")
    public void updatePerson(@RequestBody PersonRequestDto personRequestDto, @PathVariable("id") Integer id) {
        personService.updatePerson(personRequestDto, id);
    }

    @GetMapping("/get/{id}")
    public Person getPersonById(@PathVariable("id") Integer id) {
        return personService.getPersonById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleterPerson(@PathVariable("id") Integer id,
                              @RequestParam(name = "strategy", defaultValue = "pass") String strategy) {
        personService.deletePerson(id, strategy.equals("delete"));
    }
}
