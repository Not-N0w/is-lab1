package com.github.not.n0w.islab1.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoordinatesRequestDto {
    @NotNull
    private Integer x;

    @NotNull
    private Long y;
}
