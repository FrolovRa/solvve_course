package com.solvve.course.dto.principal;

import com.solvve.course.domain.constant.Role;
import lombok.Data;

@Data
public class PrincipalCreateDto {

    private String name;

    private String email;

    private Role role;

    private Boolean blocked;
}
