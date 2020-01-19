package com.solvve.course.domain;

import com.solvve.course.domain.constant.ComplaintReason;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Complaint {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Review review;
    @Enumerated(EnumType.STRING)
    private ComplaintReason reason;

    private String comment;
}
