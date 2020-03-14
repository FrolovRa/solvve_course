package com.solvve.course.domain;

import com.solvve.course.domain.constant.ComplaintReason;
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
public class Complaint {

    @Id
    @GeneratedValue
    private UUID id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @ManyToOne
    private User user;

    private UUID entityId;

    @Enumerated(EnumType.STRING)
    private ComplaintReason reason;
}