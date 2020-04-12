package com.solvve.course.dto.movie;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class MovieCreateDto {

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 1000)
    private String description;

    @NotNull
    private LocalDate release;
}