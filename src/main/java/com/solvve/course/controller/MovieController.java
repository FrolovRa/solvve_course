package com.solvve.course.controller;

import com.solvve.course.controller.documentation.ApiPageable;
import com.solvve.course.dto.PageResult;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieFilter;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public MovieReadDto getMovie(@PathVariable UUID id) {
        return movieService.getMovie(id);
    }

    @GetMapping
    @ApiPageable
    public PageResult<MovieReadDto> getMovies(MovieFilter filter, @ApiIgnore Pageable pageable) {
        return movieService.getMovies(filter, pageable);
    }

    @PostMapping
    public MovieReadDto addMovie(@RequestBody @Valid MovieCreateDto movieCreateDto) {
        return movieService.addMovie(movieCreateDto);
    }

    @PatchMapping("/{id}")
    public MovieReadDto patchMovie(@PathVariable UUID id,
                                   @RequestBody MoviePatchDto moviePatchDto) {
        return movieService.patchMovie(id, moviePatchDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable UUID id) {
        movieService.deleteMovie(id);
    }
}