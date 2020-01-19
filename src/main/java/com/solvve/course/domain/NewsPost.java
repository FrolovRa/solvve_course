package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class NewsPost {

    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    private Principal manager;

    private String title;

    private String content;

    @ManyToMany
    private List<Movie> movies = new ArrayList<>();
    @ManyToMany
    private List<Person> persons = new ArrayList<>();
    @ManyToMany
    private List<User> liked = new ArrayList<>();
}
