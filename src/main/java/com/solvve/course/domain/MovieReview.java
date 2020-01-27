package com.solvve.course.domain;

import com.solvve.course.domain.constant.ReviewStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class MovieReview {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Principal moderator;

    private String content;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @OneToMany(mappedBy = "review")
    private List<MovieReviewComplaint> movieReviewComplaints = new ArrayList<>();

    @ManyToMany(mappedBy = "likedReviews", cascade = CascadeType.PERSIST)
    private List<User> liked = new ArrayList<>();

    @ManyToOne
    private Movie movie;
}
