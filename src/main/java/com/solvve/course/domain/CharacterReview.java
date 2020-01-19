package com.solvve.course.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@DiscriminatorValue("CHARACTER_REVIEW")
public class CharacterReview extends Review {
    @ManyToOne
    private Character character;
}
