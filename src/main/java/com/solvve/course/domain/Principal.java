package com.solvve.course.domain;

import com.solvve.course.domain.constant.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Principal extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean blocked;
}
