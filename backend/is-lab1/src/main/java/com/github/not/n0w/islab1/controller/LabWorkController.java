package com.github.not.n0w.islab1.controller;

import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.MinimalPointGroup;
import com.github.not.n0w.islab1.model.dto.LabWorkRequestDto;
import com.github.not.n0w.islab1.service.LabWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/lab-works")
@RequiredArgsConstructor
@Slf4j
public class LabWorkController {
    private final LabWorkService labWorkService;

    @PostMapping("/create")
    public void createLabWork(@RequestBody LabWorkRequestDto labWork) {
        log.info("Received create request for lab work: {}", labWork);
        labWorkService.createLabWork(labWork);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLabWork(@PathVariable("id") Long id,
                              @RequestParam(name = "strategy", defaultValue = "pass") String strategy) {
        log.info("Received delete request for lab work with id: {}", id);
        labWorkService.deleteLabWork(id, strategy.equals("delete"));
    }

    @PatchMapping("/update/{id}")
    public void updateLabWork(@RequestBody LabWorkRequestDto labWorkReq, @PathVariable("id") Integer id) {
        log.info("Received update request for lab work with id: {}", id);
        labWorkService.updateLabWork(labWorkReq, id);
    }

    @GetMapping("/get/{id}")
    public LabWork getLabWorkById(@PathVariable("id") Long id) {
        log.info("Received get request for lab work with id: {}", id);
        return labWorkService.getLabWorkById(id);
    }

    @GetMapping("/all")
    public List<LabWork> getAllLabWorks() {
        log.info("Received get request for all lab works");
        return labWorkService.getAllLabWorks();
    }

    @GetMapping("/special/sum-of-minimal-points")
    public Double getSumOfMinimalPoints() {
        var response =labWorkService.getSumOfMinimalPoints();
        log.info("Received get request for sum of minimal points: {}", response);
        return response;
    }

    @GetMapping("/special/get-labs-grouped-by-minimal-points")
    public List<MinimalPointGroup> countByMinimalPoint() {
        var response =labWorkService.countByMinimalPoint();
        log.info("Received get request for labs grouped by minimal points: {}", response);
        return response;
    }

    @GetMapping("/special/get-unique-authors-ids")
    public List<Integer> getUniqueAuthors() {
        var response = labWorkService.getUniqueAuthors();
        log.info("Received get request for unique authors ids: {}", response);
        return response;
    }
    @PostMapping("/special/add-lab-to-discipline")
    public void addLabToDiscipline(@RequestParam("labId") Integer labId,
                                   @RequestParam("disciplineId") Integer disciplineId) {
        log.info("Received add lab to discipline request for labId: {}, disciplineId: {}", labId, disciplineId);
        labWorkService.addLabToDiscipline(labId, disciplineId);
    }


    @PostMapping("/special/add-top-labs-to-discipline")
    public void addTopLabsToDiscipline(@RequestParam("disciplineId") Integer disciplineId) {
        log.info("Received add top labs to discipline request for disciplineId: {}", disciplineId);
        labWorkService.addTopLabsToDiscipline(disciplineId);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
