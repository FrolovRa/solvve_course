package com.solvve.course.dto.principal;

import com.solvve.course.domain.constant.Role;
import lombok.Data;

@Data
public class PrincipalPatchDto {

    private String name;

    private String email;

    private Role role;

    private boolean blocked;
}
