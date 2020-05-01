package com.solvve.course.client.themoviedb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.solvve.course.client.themoviedb.TheMovieDbClientConfig;
import com.solvve.course.domain.constant.Genre;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieReadDto {

    private String id;

    private String originalTitle;

    private String title;

    private String overview;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;

    @JsonDeserialize(using = TheMovieDbClientConfig.GenresDeserializer.class)
    private List<Genre> genres;
}
