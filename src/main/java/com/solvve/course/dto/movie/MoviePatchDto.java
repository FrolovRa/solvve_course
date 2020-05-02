package com.solvve.course.dto.movie;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MoviePatchDto {

    private String name;

    private String description;

    private LocalDate releaseDate;
}