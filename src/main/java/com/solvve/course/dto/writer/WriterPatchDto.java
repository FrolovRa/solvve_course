package com.solvve.course.dto.writer;

import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WriterPatchDto {

    private PersonReadDto person;

    private List<MovieReadDto> movies = new ArrayList<>();
}
