package com.solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.MovieService;
import com.solvve.course.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    private TestUtils utils = new TestUtils();
    @MockBean
    private MovieService movieService;

    @Test
    public void testGetMovie() throws Exception {
        MovieReadDto movieReadDto = utils.createMovieReadDto();
        when(movieService.getMovie(movieReadDto.getId())).thenReturn(movieReadDto);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}", movieReadDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MovieReadDto actualMovie = objectMapper.readValue(resultJson, MovieReadDto.class);

        assertEquals(actualMovie, movieReadDto);
    }

    @Test
    public void testGetMovieByWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        RuntimeException exception = new EntityNotFoundException(Movie.class, wrongId);
        when(movieService.getMovie(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieWithNotValidId() throws Exception {
        mvc.perform(get("/api/v1/movies/{id}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(movieService);
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieCreateDto movieCreateDto = utils.createMovieCreateDto();
        MovieReadDto movieReadDto = utils.createMovieReadDto();

        when(movieService.addMovie(movieCreateDto)).thenReturn(movieReadDto);

        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(movieCreateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MovieReadDto actualMovieReadDto = objectMapper.readValue(resultJson, MovieReadDto.class);
        assertThat(actualMovieReadDto).isEqualToComparingFieldByField(movieReadDto);
    }

    @Test
    public void testPatchMovie() throws Exception {
        UUID id = UUID.randomUUID();
        MoviePatchDto moviePatchDto = new MoviePatchDto();
        moviePatchDto.setName("Epic");
        moviePatchDto.setDescription("test Description");
        moviePatchDto.setGenres(new HashSet<>(Arrays.asList(Genre.COMEDY, Genre.WESTERN)));

        MovieReadDto movieReadDto = utils.createMovieReadDto();

        when(movieService.patchMovie(id, moviePatchDto)).thenReturn(movieReadDto);

        String resultJson = mvc.perform(patch("/api/v1/movies/" + id)
                .content(objectMapper.writeValueAsString(moviePatchDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        MovieReadDto actualMovieReadDto = objectMapper.readValue(resultJson, MovieReadDto.class);
        assertThat(actualMovieReadDto).isEqualToComparingFieldByField(movieReadDto);
    }

    @Test
    public void testDeleteMovie() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movies/" + id)).andExpect(status().isOk());

        verify(movieService).deleteMovie(id);
    }

    @Test
    public void testGetMoviesByGenre() throws Exception {
        MovieReadDto comedyMovie = utils.createMovieReadDto();
        List<MovieReadDto> movies = Collections.singletonList(comedyMovie);
        when(movieService.findMoviesByGenre(Genre.COMEDY)).thenReturn(movies);

        String result = mvc.perform(get("/api/v1/movies/by-genre/" + Genre.COMEDY))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String actualList = objectMapper.writeValueAsString(movies);
        assertEquals(actualList, result);
    }

    @Test
    public void testGetMovieByGenreWithNotValidParam() throws Exception {
        mvc.perform(get("/api/v1/movies/by-genre/{genre}", 42)).andExpect(status().isBadRequest());

        verifyNoInteractions(movieService);
    }
}