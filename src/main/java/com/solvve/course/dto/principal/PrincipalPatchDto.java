package com.solvve.course.dto.principal;

import lombok.Data;

@Data
public class PrincipalPatchDto {

    private String name;

    private String email;

    private String password;

    private Boolean blocked;
}
