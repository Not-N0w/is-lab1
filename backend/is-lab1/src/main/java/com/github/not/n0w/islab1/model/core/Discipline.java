package com.github.not.n0w.islab1.model.core;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Discipline implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name must not be empty")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Column(name = "practice_hours")
    private Integer practiceHours; //Поле может быть null
}