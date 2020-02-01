package com.solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private Principal principal;

    private boolean blockedReview;

    private int trustLevel;
}
