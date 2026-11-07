package com.github.not.n0w.islab1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationRequestDto {
    @NotNull(message = "X is required")
    private Float x;

    @NotNull(message = "Y is required")
    private Float y;

    @NotNull(message = "Z is required")
    private Double z;

    private String name;
}
