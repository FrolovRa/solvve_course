package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Character extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private Actor actor;

    @NotNull
    @ManyToOne
    private Movie movie;
}
