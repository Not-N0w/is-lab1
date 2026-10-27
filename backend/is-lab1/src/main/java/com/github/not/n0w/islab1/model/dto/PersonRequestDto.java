package com.github.not.n0w.islab1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.not.n0w.islab1.model.core.Color;
import com.github.not.n0w.islab1.model.core.Country;
import com.github.not.n0w.islab1.model.core.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonRequestDto {
    @NotBlank
    private String name;

    @NotNull
    private Color eyeColor;

    private Color hairColor;

    @Valid
    private Location location;
    private Integer locationId;

    @NotNull
    @Min(value = 1, message = "Age must be greater than 0")
    private Float height;

    @NotNull
    private Country nationality;

    @JsonIgnore
    @AssertTrue(message = "Location are required")
    public boolean isLocationPresent() {
        return location != null || locationId != null;
    }

    public boolean isNewLocation() {
        return (locationId == null || location != null);
    }
}
