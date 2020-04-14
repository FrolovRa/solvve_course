package com.solvve.course.dto.principal;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PrincipalReadDto {

    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

    private String name;

    private String email;

    private Boolean blocked;
}
