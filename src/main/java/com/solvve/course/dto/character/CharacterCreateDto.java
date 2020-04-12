package com.solvve.course.dto.character;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class CharacterCreateDto {

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private UUID actorId;

    @NotNull
    private UUID movieId;
}
