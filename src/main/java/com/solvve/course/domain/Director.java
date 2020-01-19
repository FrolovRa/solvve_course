package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Director {

    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    private Person person;
    @ManyToMany
    private List<Movie> movies = new ArrayList<>();
}
