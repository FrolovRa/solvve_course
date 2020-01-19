package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@DiscriminatorValue("MOVIE_REVIEW")
public class MovieReview extends Review {

    @ManyToOne
    private Movie movie;
}
