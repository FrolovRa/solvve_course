package com.solvve.course.dto.rating;

import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

@Data
public class CharacterRatingPairPatchDto {

    private UserReadDto user;

    private CharacterReadDto character;

    private double value;
}
