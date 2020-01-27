package com.solvve.course.dto.principal;

import com.solvve.course.domain.constant.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class PrincipalCreateDto {

    private String name;

    private String email;

    private Role role;

    private boolean blocked;
}
