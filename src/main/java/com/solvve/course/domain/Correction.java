package com.solvve.course.domain;

import com.solvve.course.domain.constant.CorrectionStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Correction {

    @Id
    @GeneratedValue
    private UUID id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

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