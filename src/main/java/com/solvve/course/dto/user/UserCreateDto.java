package com.solvve.course.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserCreateDto {

    private UUID principalId;

    private Boolean blockedReview;

    private Integer trustLevel;
}
