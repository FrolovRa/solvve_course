package com.solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.solvve.course.BaseControllerTest;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.PageResult;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieFilter;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.service.MovieService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieController.class)
public class MovieControllerTest extends BaseControllerTest {

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
    public void testGetMoviesByFilter() throws Exception {
        MovieFilter filter = new MovieFilter();
        filter.setActorId(UUID.randomUUID());
        filter.setGenres(Set.of(Genre.ADVENTURE, Genre.CRIME));
        filter.setName("star wars");
        filter.setReleaseDateFrom(LocalDate.now());
        filter.setReleaseDateTo(LocalDate.now());

        MovieReadDto movie = utils.createMovieReadDto();
        List<MovieReadDto> expectedData = Collections.singletonList(movie);

        PageResult<MovieReadDto> expected = new PageResult<>();
        expected.setData(expectedData);
        when(movieService.getMovies(filter, PageRequest.of(0, defaultPageSize))).thenReturn(expected);

        String resultJson = mvc.perform(get("/api/v1/movies")
            .param("name", filter.getName())
            .param("actorId", filter.getActorId().toString())
            .param("genres", "ADVENTURE, CRIME")
            .param("releaseDateFrom", filter.getReleaseDateFrom().toString())
            .param("releaseDateTo", filter.getReleaseDateTo().toString()))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        PageResult<MovieReadDto> actual = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        assertEquals(expected, actual);
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
    public void testGetMoviesByFilterWithPagingAndSorting() throws Exception {
        MovieFilter filter = new MovieFilter();

        MovieReadDto movie = utils.createMovieReadDto();
        List<MovieReadDto> expectedData = Collections.singletonList(movie);

        final int page = 1;
        final int size = 25;

        PageResult<MovieReadDto> expected = new PageResult<>();
        expected.setPage(page);
        expected.setTotalPages(4);
        expected.setPageSize(size);
        expected.setTotalElements(100);
        expected.setData(expectedData);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "name");
        when(movieService.getMovies(filter, pageRequest)).thenReturn(expected);

        String resultJson = mvc.perform(get("/api/v1/movies")
            .param("page", Integer.toString(page))
            .param("size", Integer.toString(size))
            .param("sort", "name,desc"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        PageResult<MovieReadDto> actual = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMoviesByFilterWithBigPage() throws Exception {
        MovieFilter filter = new MovieFilter();

        MovieReadDto movie = utils.createMovieReadDto();
        List<MovieReadDto> expectedData = Collections.singletonList(movie);

        final int page = 0;
        final int size = 99999;

        PageResult<MovieReadDto> expected = new PageResult<>();
        expected.setPage(page);
        expected.setTotalPages(4);
        expected.setPageSize(size);
        expected.setTotalElements(100);
        expected.setData(expectedData);

        PageRequest pageRequest = PageRequest.of(page, maxPageSize);
        when(movieService.getMovies(filter, pageRequest)).thenReturn(expected);

        String resultJson = mvc.perform(get("/api/v1/movies")
            .param("page", Integer.toString(page))
            .param("size", Integer.toString(size)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        PageResult<MovieReadDto> actual = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        assertEquals(expected, actual);
    }
}