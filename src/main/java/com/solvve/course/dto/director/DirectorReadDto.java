package com.solvve.course.dto.director;

import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class DirectorReadDto {

    private UUID id;

    private PersonReadDto person;

    private List<MovieReadDto> movies = new ArrayList<>();
}
