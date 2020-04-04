package com.solvve.course.dto.character;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CharacterCreateDto {

    @NotNull
    private String name;

    @NotNull
    private UUID actorId;

    @NotNull
    private UUID movieId;
}
