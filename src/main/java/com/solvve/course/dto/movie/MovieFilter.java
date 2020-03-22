package com.solvve.course.dto.movie;

import com.solvve.course.domain.constant.Genre;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class MovieFilter {

    private String name;

    private UUID actorId;

    private Set<Genre> genres;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDateTo;
}
