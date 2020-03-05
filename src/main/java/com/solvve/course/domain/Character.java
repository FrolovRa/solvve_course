package com.solvve.course.domain;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;


@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Character {

    @Id
    @GeneratedValue
    private UUID id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String name;

    @ToString.Exclude
    @ManyToOne
    private Actor actor;

    @ToString.Exclude
    @ManyToOne
    private Movie movie;

}
