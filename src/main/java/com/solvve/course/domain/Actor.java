package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Actor {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private Person person;

    @ManyToMany
    private List<Movie> movies = new ArrayList<>();

    @ManyToMany
    private List<Movie> moviesAsStar = new ArrayList<>();

    @OneToMany(mappedBy = "actor", cascade = CascadeType.PERSIST)
    private List<Character> characters = new ArrayList<>();
}
