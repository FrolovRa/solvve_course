package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity {

    @NotNull
    @OneToOne
    private Principal principal;

    private Boolean blockedReview;

    private Integer trustLevel;
}