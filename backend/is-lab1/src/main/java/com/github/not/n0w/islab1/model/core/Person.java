package com.github.not.n0w.islab1.model.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Enumerated(EnumType.STRING)
    private Color eyeColor; //Поле не может быть null

    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле может быть null

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location; //Поле не может быть null
    private float height; //Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле может быть null
}