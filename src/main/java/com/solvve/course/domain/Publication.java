package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Publication extends AbstractEntity {

    @NotNull
    private String content;

    @NotNull
    @OneToOne
    private Principal manager;

    @OneToMany(mappedBy = "publication")
    private List<Correction> corrections;
}