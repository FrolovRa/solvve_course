package com.solvve.course.dto.principal;

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

    private Boolean blocked;
}
