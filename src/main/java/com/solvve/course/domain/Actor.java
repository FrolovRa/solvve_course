package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Actor extends AbstractEntity {

    @OneToOne
    private Person person;

    @ManyToMany(mappedBy = "cast", cascade = CascadeType.PERSIST)
    private List<Movie> movies = new ArrayList<>();

    @ManyToMany(mappedBy = "stars", cascade = CascadeType.PERSIST)
    private List<Movie> moviesAsStar = new ArrayList<>();

    @OneToMany(mappedBy = "actor", cascade = CascadeType.PERSIST)
    private List<Character> characters = new ArrayList<>();
}
