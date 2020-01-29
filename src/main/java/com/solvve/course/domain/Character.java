package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
public class Character {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    @ManyToOne
    private Actor actor;

    @ManyToOne
    private Movie movie;
}
