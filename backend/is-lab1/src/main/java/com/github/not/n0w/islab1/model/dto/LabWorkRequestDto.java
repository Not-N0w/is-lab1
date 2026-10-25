package com.github.not.n0w.islab1.model.dto;

import com.github.not.n0w.islab1.model.core.Coordinates;
import com.github.not.n0w.islab1.model.core.Difficulty;
import com.github.not.n0w.islab1.model.core.Discipline;
import com.github.not.n0w.islab1.model.core.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import jakarta.validation.Valid;

@Data
public class LabWorkRequestDto {
    @NotBlank
    private String name;

    @Valid
    private Coordinates coordinates;
    private Integer coordinatesId;

    @NotNull
    private String description;

    @NotNull
    private Difficulty difficulty;

    @Valid
    private Discipline discipline;
    private Integer disciplineId;

    @PositiveOrZero
    private Double minimalPoint;

    @PositiveOrZero
    private Integer tunedInWorks;

    @Valid
    private Person author;
    private Integer authorId;

    public boolean isNewCoordinates() {
        return (coordinatesId == null || coordinates != null);
    }

    public boolean isNewDiscipline() {
        return (disciplineId == null || discipline != null);
    }

    public boolean isNewAuthor() {
        return (authorId == null || author != null);
    }
}
