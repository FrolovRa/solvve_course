package com.solvve.course.dto.character;

import lombok.Data;

import java.util.UUID;

@Data
public class CharacterPatchDto {

    private String name;

    private UUID actorId;

    private UUID movieId;
}
