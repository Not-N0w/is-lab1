package com.github.not.n0w.islab1.model.dto;

import com.github.not.n0w.islab1.model.core.Color;
import com.github.not.n0w.islab1.model.core.Country;
import com.github.not.n0w.islab1.model.core.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PersonRequestDto {
    @NotBlank
    private String name;

    @NotNull
    private Color eyeColor;

    @NotNull
    private Color hairColor;

    @Valid
    private Location location;
    private Integer locationId;

    @NotNull
    @Positive
    private Float height;

    @NotNull
    private Country nationality;

    private String strategy;

    public boolean isNewLocation() {
        return (locationId == null || location != null);
    }
}
