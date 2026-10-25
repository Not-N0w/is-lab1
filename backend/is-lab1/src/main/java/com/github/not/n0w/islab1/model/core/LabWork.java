package com.github.not.n0w.islab1.model.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private String description; //Поле может быть null

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty; //Поле может быть null

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline; //Поле не может быть null
    private Double minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private Integer tunedInWorks; //Поле не может быть null

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private Person author; //Поле может быть null
}