package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Person extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthDate;

    private String biography;
}
