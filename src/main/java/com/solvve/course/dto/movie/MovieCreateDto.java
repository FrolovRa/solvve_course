package com.solvve.course.dto.movie;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class MovieCreateDto {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private LocalDate release;
}