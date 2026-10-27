package com.github.not.n0w.islab1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.core.Difficulty;
import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.core.Person;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabWorkRequestDto {
    @NotBlank(message = "Name must not be empty")
    private String name;

    @Valid
    private Coordinates coordinates;
    private Integer coordinatesId;

    private String description;
    private Difficulty difficulty;

    @Valid
    private Discipline discipline;
    private Integer disciplineId;

    @NotNull(message = "Minimal point is required")
    @Positive(message = "Minimal point must be greater than 0")
    private Double minimalPoint;

    @NotNull(message = "Tuned in works is required")
    private Integer tunedInWorks;

    @Valid
    private Person author;
    private Integer authorId;

    @JsonIgnore
    @AssertTrue(message = "Coordinates are required")
    public boolean isCoordinatesPresent() {
        return coordinates != null || coordinatesId != null;
    }

    @JsonIgnore
    @AssertTrue(message = "Discipline is required")
    public boolean isDisciplinePresent() {
        return discipline != null || disciplineId != null;
    }

    @JsonIgnore
    public boolean isNewCoordinates() {
        return coordinatesId == null || coordinates != null;
    }

    @JsonIgnore
    public boolean isNewDiscipline() {
        return disciplineId == null || discipline != null;
    }

    @JsonIgnore
    public boolean isNewAuthor() {
        return authorId == null || author != null;
    }
}