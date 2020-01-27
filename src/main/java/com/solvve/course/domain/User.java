package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private Principal principal;

    private boolean blockedReview;

    private int trustLevel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<MovieRatingPair> ratedMovies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<CharacterRatingPair> ratedCharacter = new ArrayList<>();

    @ManyToMany(mappedBy = "liked", cascade = CascadeType.PERSIST)
    private List<Publication> likedPosts = new ArrayList<>();

    @ManyToMany
    private List<MovieReview> likedMovieReviews = new ArrayList<>();

    @ManyToMany
    private List<CharacterReview> likedCharacterReviews = new ArrayList<>();
}
