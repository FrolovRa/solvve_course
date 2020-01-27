package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    private double rating;
    @ManyToOne
    private Movie movie;

    @OneToMany(mappedBy = "character")
    private List<CharacterReview> reviews = new ArrayList<>();
}
