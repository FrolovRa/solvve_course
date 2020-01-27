package com.solvve.course.domain;

import com.solvve.course.domain.constant.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Principal {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean blocked;
}
