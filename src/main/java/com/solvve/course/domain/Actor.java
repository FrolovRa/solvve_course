package com.solvve.course.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Actor {

    @Id
    @GeneratedValue
    private UUID id;

    @CreatedDate
    private Instant createdAt;

    @OneToOne(targetEntity = Person.class)
    private Person person;

    @ManyToMany
    private List<Movie> movies = new ArrayList<>();

    @ManyToMany
    private List<Movie> moviesAsStar = new ArrayList<>();

    @OneToMany(mappedBy = "actor", cascade = CascadeType.PERSIST)
    private List<Character> characters = new ArrayList<>();
}
