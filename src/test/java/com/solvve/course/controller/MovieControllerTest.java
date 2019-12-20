package com.solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.dto.MovieReadDto;
import com.solvve.course.service.MovieService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;

    @Test
    public void testGetMovie() throws Exception {
        MovieReadDto movieReadDto = new MovieReadDto();
        movieReadDto.setId(UUID.randomUUID());
        movieReadDto.setRating(7.8d);
        movieReadDto.setName("Mr.Nobody");
        movieReadDto.setGenre("Drama");
        movieReadDto.setMainActor("Jared Leto");
        when(movieService.getMovie(movieReadDto.getId())).thenReturn(movieReadDto);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}", movieReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MovieReadDto actualMovie = objectMapper.readValue(resultJson, MovieReadDto.class);

        assertEquals(actualMovie, movieReadDto);
    }
}
