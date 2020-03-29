package com.solvve.course.domain;

import com.solvve.course.domain.constant.CorrectionStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Correction extends AbstractEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Publication publication;

    private Integer startIndex;

    private String selectedText;

    private String proposedText;

    @Enumerated(EnumType.STRING)
    private CorrectionStatus status;
}