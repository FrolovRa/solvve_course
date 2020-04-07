package com.solvve.course.domain;

import com.solvve.course.domain.constant.CorrectionStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Correction extends AbstractEntity {

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Publication publication;

    @NotNull
    private Integer startIndex;

    @NotNull
    private String selectedText;

    private String proposedText;

    @Enumerated(EnumType.STRING)
    private CorrectionStatus status;
}