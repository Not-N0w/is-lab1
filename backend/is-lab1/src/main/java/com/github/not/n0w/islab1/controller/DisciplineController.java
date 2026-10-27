package com.github.not.n0w.islab1.controller;

import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.dto.DisciplineRequestDto;
import com.github.not.n0w.islab1.service.DisciplineService;
import com.github.not.n0w.islab1.service.WebSocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disciplines")
@RequiredArgsConstructor
public class DisciplineController {
    private final DisciplineService disciplineService;


    @RequestMapping("/all")
    public Iterable<Discipline> getAllDisciplines() {
        return disciplineService.getAllDisciplines();
    }

    @RequestMapping("/create")
    public void createDiscipline(@Valid @RequestBody DisciplineRequestDto disciplineDto) {
        disciplineService.createDiscipline(disciplineDto);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteDiscipline(@PathVariable("id") Integer id) {
        disciplineService.deleteDiscipline(id);
    }

    @PatchMapping("/update/{id}")
    public void updateDiscipline(@Valid @RequestBody DisciplineRequestDto disciplineDto, @PathVariable("id") Integer id) {
        disciplineService.updateDiscipline(disciplineDto, id);
    }

    @GetMapping("/{id}")
    public Discipline getDisciplineById(@PathVariable("id") Integer id) {
        return disciplineService.getDisciplineById(id);
    }
}
