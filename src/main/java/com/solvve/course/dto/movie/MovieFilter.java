package com.solvve.course.dto.movie;

import com.solvve.course.domain.constant.Genre;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class MovieFilter {

    private String name;

    private UUID actorId;

    private Set<Genre> genres;

    private LocalDate releaseDateFrom;

    private LocalDate releaseDateTo;
}
