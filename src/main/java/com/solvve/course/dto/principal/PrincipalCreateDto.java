package com.solvve.course.dto.principal;

import com.solvve.course.domain.constant.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class PrincipalCreateDto {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private Role role;

    private Boolean blocked;
}
