package com.solvve.course.dto.role;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.constant.Role;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PrincipalRoleReadDto {

    private UUID id;

    private Role role;

    private Principal principal;

    private Instant createdAt;

    private Instant updatedAt;
}
