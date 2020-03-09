package com.solvve.course.domain;

import com.solvve.course.domain.constant.Genre;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    @GeneratedValue
    private UUID id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String name;

    private String description;

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
