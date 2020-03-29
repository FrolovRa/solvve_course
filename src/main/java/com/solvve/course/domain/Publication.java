package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Publication extends AbstractEntity {

    @OneToOne
    private Principal manager;

    private String content;

    @OneToMany(mappedBy = "publication")
    private List<Correction> corrections;
}