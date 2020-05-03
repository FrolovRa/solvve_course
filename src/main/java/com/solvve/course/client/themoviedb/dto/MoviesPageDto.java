package com.solvve.course.client.themoviedb.dto;

import lombok.Data;

import java.util.List;

@Data
public class MoviesPageDto {

    private Integer page;
    private Integer totalResults;
    private Integer totalPages;
    private List<MovieReadShortDto> results;
}
