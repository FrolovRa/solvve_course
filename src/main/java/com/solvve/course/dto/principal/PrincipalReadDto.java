package com.solvve.course.dto.principal;

import com.solvve.course.domain.constant.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class PrincipalReadDto {

    private UUID id;

    private String name;

    private String email;

    private Role role;

    private Boolean blocked;
}
