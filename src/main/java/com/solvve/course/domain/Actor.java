package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Actor extends AbstractEntity {

    @NotNull
    @OneToOne
    private Person person;

    @ManyToMany(mappedBy = "cast")
    private List<Movie> movies = new ArrayList<>();

    @ManyToMany(mappedBy = "stars")
    private List<Movie> moviesAsStar = new ArrayList<>();

    @OneToMany(mappedBy = "actor", cascade = CascadeType.PERSIST)
    private List<Character> characters = new ArrayList<>();
}