package com.solvve.course.dto.rating;

import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.util.UUID;

@Data
public class CharacterRatingPairReadDto {

    private UUID id;

    private UserReadDto user;

    private CharacterReadDto character;

    private double value;
}
