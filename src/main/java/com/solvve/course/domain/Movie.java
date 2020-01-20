package com.solvve.course.domain;

import com.solvve.course.domain.constant.Genre;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Movie {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String description;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private List<Character> characters = new ArrayList<>();

    @ManyToMany(mappedBy = "movies", cascade = CascadeType.PERSIST)
    private List<Actor> cast = new ArrayList<>();

    @ManyToMany(mappedBy = "moviesAsStar", cascade = CascadeType.PERSIST)
    private List<Actor> stars = new ArrayList<>();

    @ManyToMany(mappedBy = "movies", cascade = CascadeType.PERSIST)
    private List<Director> directors = new ArrayList<>();

    @ManyToMany(mappedBy = "movies", cascade = CascadeType.PERSIST)
    private List<Writer> writers = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private List<MovieReview> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "movies", cascade = CascadeType.PERSIST)
    private List<NewsPost> posts = new ArrayList<>();
}
