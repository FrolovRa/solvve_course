package com.solvve.course.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;


@Entity
@Data
public class Character {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    @ToString.Exclude
    @ManyToOne
    private Actor actor;
    @ToString.Exclude
    @ManyToOne
    private Movie movie;

}
