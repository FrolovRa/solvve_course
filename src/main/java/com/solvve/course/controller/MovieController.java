package com.solvve.course.controller;

import com.solvve.course.dto.MovieCreateDto;
import com.solvve.course.dto.MovieReadDto;
import com.solvve.course.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping("/{id}")
    public MovieReadDto getMovie(@PathVariable UUID id) {
        return movieService.getMovie(id);
    }

    @PostMapping
    public MovieReadDto addMovie(@RequestBody MovieCreateDto movieCreateDto) {
        return movieService.addMovie(movieCreateDto);
    }
}
