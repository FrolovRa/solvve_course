package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Character extends AbstractEntity {

    private String name;

    @ManyToOne
    private Actor actor;

    @ManyToOne
    private Movie movie;
}
