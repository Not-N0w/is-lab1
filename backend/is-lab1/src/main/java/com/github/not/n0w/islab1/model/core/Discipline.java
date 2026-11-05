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
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Column(name = "practice_hours")
    private Integer practiceHours; //Поле может быть null
}