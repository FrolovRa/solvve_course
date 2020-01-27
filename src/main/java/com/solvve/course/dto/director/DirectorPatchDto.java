package com.solvve.course.dto.director;

import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DirectorPatchDto {

    private PersonReadDto person;

    private List<MovieReadDto> movies = new ArrayList<>();
}
