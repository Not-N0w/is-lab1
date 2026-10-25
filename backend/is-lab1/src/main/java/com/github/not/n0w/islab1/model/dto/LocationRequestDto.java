package com.github.not.n0w.islab1.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationRequestDto {
    @NotNull
    private Float x;

    @NotNull
    private Float y;

    @NotNull
    private Double z;

    @NotBlank
    private String name;
}
