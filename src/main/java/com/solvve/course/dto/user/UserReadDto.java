package com.solvve.course.dto.user;

import com.solvve.course.dto.principal.PrincipalReadDto;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserReadDto {

    private UUID id;

    private PrincipalReadDto principal;

    private Boolean blockedReview;

    private Integer trustLevel;

    private Instant createdAt;

    private Instant updatedAt;
}
