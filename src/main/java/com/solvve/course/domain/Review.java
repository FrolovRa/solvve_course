package com.solvve.course.domain;

import com.solvve.course.domain.constant.ReviewStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance
public abstract class Review {

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
    private List<Complaint> complaints = new ArrayList<>();
    @ManyToMany(mappedBy = "likedReviews", cascade = CascadeType.PERSIST)
    private List<User> liked = new ArrayList<>();
}
