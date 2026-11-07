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
    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Eye color is required")
    private Color eyeColor;

    private Color hairColor;

    @Valid
    private Location location;
    private Integer locationId;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be greater than 0")
    private Float height;

    private Country nationality;

    @JsonIgnore
    @AssertTrue(message = "Location is required")
    public boolean isLocationPresent() {
        return location != null || locationId != null;
    }

}
