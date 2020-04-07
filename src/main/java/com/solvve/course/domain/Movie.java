package com.solvve.course.domain;

import com.solvve.course.domain.constant.Genre;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Movie extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    private String description;

    private Double rating;

    @NotNull
    private LocalDate release;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"))
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private List<Character> characters = new ArrayList<>();

    @ManyToMany
    private List<Actor> cast = new ArrayList<>();

    @ManyToMany
    private List<Actor> stars = new ArrayList<>();
}
