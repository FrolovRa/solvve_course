package com.solvve.course.dto.user;

import com.solvve.course.dto.principal.PrincipalReadDto;
import lombok.Data;

@Data
public class UserCreateDto {

    private PrincipalReadDto principal;

    private boolean blockedReview;

    private int trustLevel;
}
