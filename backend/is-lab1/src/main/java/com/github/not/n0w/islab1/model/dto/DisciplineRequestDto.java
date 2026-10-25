package com.github.not.n0w.islab1.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DisciplineRequestDto {
    @NotBlank
    private String name;

    @Positive
    private Integer practiceHours;
}
