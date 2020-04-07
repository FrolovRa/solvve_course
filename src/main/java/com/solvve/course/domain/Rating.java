package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Rating extends AbstractEntity {

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    private UUID entityId;

    @NotNull
    private Double rating;
}
