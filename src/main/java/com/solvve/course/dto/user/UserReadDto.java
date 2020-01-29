package com.solvve.course.dto.user;

import com.solvve.course.dto.principal.PrincipalReadDto;
import lombok.Data;

import java.util.UUID;

@Data
public class UserReadDto {

    private UUID id;

    private PrincipalReadDto principal;

    private boolean blockedReview;

    private int trustLevel;
}
