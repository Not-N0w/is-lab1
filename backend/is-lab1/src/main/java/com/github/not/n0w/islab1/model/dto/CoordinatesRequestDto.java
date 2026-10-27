package com.github.not.n0w.islab1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoordinatesRequestDto {

    @NotNull(message = "X is required")
    @Min(value = -211, message = "X must be greater than -212")
    private Integer x;

    @NotNull(message = "Y is required")
    private Long y;
}