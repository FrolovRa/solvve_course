package com.solvve.course.dto.writer;

import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class WriterReadDto {

    private UUID id;

    private PersonReadDto person;

    private List<MovieReadDto> movies = new ArrayList<>();
}
