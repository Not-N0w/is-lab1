package com.github.not.n0w.islab1.model.core;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "X is required")
    private Float x; //Поле не может быть null

    @NotNull(message = "Y is required")
    private Float y; //Поле не может быть null

    @NotNull(message = "Z is required")
    private Double z; //Поле не может быть null

    private String name; //Поле может быть null
}