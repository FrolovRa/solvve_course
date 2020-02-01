package com.solvve.course.service;

import com.solvve.course.domain.Movie;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.MovieRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from actor",
        "delete from movie",
        "delete from person"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private MovieService movieService;

    @Test
    @Transactional
    public void testGetMovie() {
        Movie movie = utils.getMovieFromDb();

        movie = movieRepository.save(movie);

        MovieReadDto movieReadDto = movieService.getMovie(movie.getId());
        assertThat(movieReadDto).isEqualToComparingFieldByField(movie);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieByWrongId() {
        movieService.getMovie(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testAddMovie() {
        MovieCreateDto movieCreateDto = utils.createMovieCreateDto();

        MovieReadDto movieReadDto = movieService.addMovie(movieCreateDto);

        assertThat(movieCreateDto).isEqualToComparingFieldByField(movieReadDto);
        assertNotNull(movieReadDto.getId());

        Movie movieFromDb = movieRepository.findById(movieReadDto.getId()).get();
        assertThat(movieReadDto).isEqualToComparingFieldByField(movieFromDb);
    }

    @Test
    @Transactional
    public void testPatchMovie() {
        MoviePatchDto moviePatchDto = new MoviePatchDto();
        moviePatchDto.setName("Epic");
        moviePatchDto.setDescription("test Description");
        moviePatchDto.setRelease(LocalDate.now());
        moviePatchDto.setGenres(new HashSet<>(Arrays.asList(Genre.COMEDY, Genre.WESTERN)));
        moviePatchDto.setCast(Collections.singletonList(translationService.toReadDto(utils.getActorFromDb())));
        moviePatchDto.setStars(Collections.singletonList(translationService.toReadDto(utils.getActorFromDb())));
        moviePatchDto.setCharacters(Collections.emptyList());

        Movie movieFromDb = utils.getMovieFromDb();
        MovieReadDto patchedMovie = movieService.patchMovie(movieFromDb.getId(), moviePatchDto);

        assertThat(moviePatchDto).isEqualToComparingFieldByField(patchedMovie);
    }

    @Test
    @Transactional
    public void testEmptyPatchMovie() {
        MoviePatchDto moviePatchDto = new MoviePatchDto();

        MovieReadDto movieBeforePatch = translationService.toReadDto(utils.getMovieFromDb());

        MovieReadDto movieAfterPatch = movieService.patchMovie(movieBeforePatch.getId(), moviePatchDto);
        assertNotNull(movieAfterPatch.getDescription());
        assertNotNull(movieAfterPatch.getName());
        assertNotNull(movieAfterPatch.getGenres());

        assertThat(movieBeforePatch).isEqualToComparingFieldByField(movieAfterPatch);
    }

    @Test
    public void testDeleteMovie() {
        MovieReadDto movieReadDto = translationService.toReadDto(utils.getMovieFromDb());
        assertNotNull(movieReadDto.getId());

        movieService.deleteMovie(movieReadDto.getId());

        assertFalse(movieRepository.existsById(movieReadDto.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        movieService.deleteMovie(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testFindMoviesByGenre() {
        MovieCreateDto horrorMovie = new MovieCreateDto();
        horrorMovie.setGenres(new HashSet<>(Collections.singletonList(Genre.HORROR)));
        MovieReadDto horrorReadDto = movieService.addMovie(horrorMovie);

        MovieCreateDto comedyMovie = new MovieCreateDto();
        comedyMovie.setGenres(new HashSet<>(Collections.singletonList(Genre.COMEDY)));
        MovieReadDto comedyReadDto = movieService.addMovie(comedyMovie);

        List<MovieReadDto> result =  movieService
                .findMoviesByGenre(Genre.HORROR);
        assertEquals(1, result.size());
        assertEquals(result.get(0), horrorReadDto);
    }
}