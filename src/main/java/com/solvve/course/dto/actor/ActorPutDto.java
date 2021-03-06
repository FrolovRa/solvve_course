package com.solvve.course.dto.actor;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ActorPutDto {

    @NotNull
    private UUID personId;
}
