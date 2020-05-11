package com.solvve.course.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private String password;

    @ManyToMany
    @JoinTable(name = "principal_principal_role",
            joinColumns = @JoinColumn(name = "principal_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<PrincipalRole> roles = new ArrayList<>();

    private Boolean blocked;
}