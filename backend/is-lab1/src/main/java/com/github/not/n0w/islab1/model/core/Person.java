package com.github.not.n0w.islab1.model.core;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name must not be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Eye color is required")
    private Color eyeColor; //Поле не может быть null

    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле может быть null

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    @NotNull(message = "Location is required")
    private Location location; //Поле не может быть null

    @Positive(message = "Height must be greater than 0")
    private float height; //Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле может быть null
}