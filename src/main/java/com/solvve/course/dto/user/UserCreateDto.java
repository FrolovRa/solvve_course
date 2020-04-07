package com.solvve.course.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UserCreateDto {

    @NotNull
    private UUID principalId;

    private Boolean blockedReview;

    private Integer trustLevel;
}
