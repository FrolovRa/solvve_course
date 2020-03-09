package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.Rating;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieFilter;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.MovieRepository;
import com.solvve.course.repository.RatingRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from movie_cast",
        "delete from rating",
        "delete from movie",
        "delete from actor",
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

    @Autowired
    private RatingRepository ratingRepository;

    @Test
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
    public void testAddMovie() {
        MovieCreateDto movieCreateDto = utils.createMovieCreateDto();

        MovieReadDto movieReadDto = movieService.addMovie(movieCreateDto);

        assertThat(movieCreateDto).isEqualToComparingFieldByField(movieReadDto);
        assertNotNull(movieReadDto.getId());

        Movie movieFromDb = movieRepository.findById(movieReadDto.getId()).get();
        assertThat(movieReadDto).isEqualToComparingFieldByField(movieFromDb);
    }

    @Test
    public void testPatchMovie() {
        MoviePatchDto moviePatchDto = new MoviePatchDto();
        moviePatchDto.setName("Epic");
        moviePatchDto.setDescription("test Description");
        moviePatchDto.setRelease(LocalDate.now());

        Movie movieFromDb = utils.getMovieFromDb();
        MovieReadDto patchedMovie = movieService.patchMovie(movieFromDb.getId(), moviePatchDto);

        assertThat(moviePatchDto).isEqualToComparingFieldByField(patchedMovie);
    }

    @Test
    public void testEmptyPatchMovie() {
        MoviePatchDto moviePatchDto = new MoviePatchDto();

        MovieReadDto movieBeforePatch = translationService.toReadDto(utils.getMovieFromDb());

        MovieReadDto movieAfterPatch = movieService.patchMovie(movieBeforePatch.getId(), moviePatchDto);
        assertNotNull(movieAfterPatch.getDescription());
        assertNotNull(movieAfterPatch.getName());

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
    public void testCreatedAtIsSet() {
        Movie movie = new Movie();
        movie.setName("Time");

        movie = movieRepository.save(movie);

        Instant createdAtBeforeReload = movie.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        movie = movieRepository.findById(movie.getId()).get();

        Instant createdAtAfterReload = movie.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Movie movie = new Movie();
        movie.setName("Time");

        movie = movieRepository.save(movie);

        Instant updatedAtBeforeReload = movie.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        movie = movieRepository.findById(movie.getId()).get();

        Instant updatedAtAfterReload = movie.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        movie.setName("Money");
        movie = movieRepository.save(movie);
        Instant updatedAtAfterUpdate = movie.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }

    @Test
    public void testGetMoviesWithEmptyFilter() {
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setGenres(Stream.of(Genre.ACTION, Genre.COMEDY).collect(Collectors.toSet()));
        movie.setRelease(LocalDate.now());

        Movie secondMovie = new Movie();
        movie = movieRepository.save(movie);
        secondMovie = movieRepository.save(secondMovie);

        MovieFilter filter = new MovieFilter();

        assertThat(movieService.getMovies(filter)).extracting("id")
                .containsExactlyInAnyOrder(movie.getId(), secondMovie.getId());
    }

    @Test
    public void testGetMoviesWithFilterWithName() {
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setRelease(LocalDate.now());

        Movie secondMovie = new Movie();
        movie = movieRepository.save(movie);
        secondMovie = movieRepository.save(secondMovie);

        MovieFilter filter = new MovieFilter();
        filter.setName("movie");

        assertThat(movieService.getMovies(filter)).extracting("id")
                .containsOnly(movie.getId())
                .doesNotContain(secondMovie.getId());
    }

    @Test
    public void testGetMoviesWithFilterWithGenres() {
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setGenres(Stream.of(Genre.ACTION, Genre.COMEDY).collect(Collectors.toSet()));
        movie.setRelease(LocalDate.now());

        Movie secondMovie = new Movie();
        secondMovie.setGenres(Stream.of(Genre.ADVENTURE).collect(Collectors.toSet()));
        movie = movieRepository.save(movie);
        secondMovie = movieRepository.save(secondMovie);

        MovieFilter filter = new MovieFilter();
        filter.setGenres((Stream.of(Genre.ACTION, Genre.ADVENTURE).collect(Collectors.toSet())));

        assertThat(movieService.getMovies(filter)).extracting("id")
                .containsExactlyInAnyOrder(movie.getId(), secondMovie.getId());
    }

    @Test
    public void testGetMoviesWithFilterWithEmptyGenres() {
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setGenres(Stream.of(Genre.ACTION, Genre.COMEDY).collect(Collectors.toSet()));
        movie.setRelease(LocalDate.now());

        Movie secondMovie = new Movie();
        secondMovie.setGenres(Stream.of(Genre.ADVENTURE).collect(Collectors.toSet()));
        movie = movieRepository.save(movie);
        secondMovie = movieRepository.save(secondMovie);

        MovieFilter filter = new MovieFilter();
        filter.setGenres(Collections.emptySet());

        assertThat(movieService.getMovies(filter)).extracting("id")
                .containsExactlyInAnyOrder(movie.getId(), secondMovie.getId());
    }

    @Test
    public void testGetMoviesWithFilterWithActor() {
        Actor actor = utils.getActorFromDb();
        Movie movie = new Movie();
        movie.setCast(Collections.singletonList(actor));
        movie.setName("movie");
        movie.setDescription("description");

        Movie secondMovie = new Movie();
        secondMovie.setGenres(Stream.of(Genre.ADVENTURE).collect(Collectors.toSet()));
        movie = movieRepository.save(movie);
        secondMovie = movieRepository.save(secondMovie);

        MovieFilter filter = new MovieFilter();
        filter.setActorId(actor.getId());

        assertThat(movieService.getMovies(filter)).extracting("id")
                .containsOnly(movie.getId())
                .doesNotContain(secondMovie.getId());
    }

    @Test
    public void testGetMoviesWithFilterWithReleaseDate() {
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setRelease(LocalDate.of(2000, 1, 10));

        Movie secondMovie = new Movie();
        secondMovie.setName("movie2");
        secondMovie.setDescription("description2");
        secondMovie.setRelease(LocalDate.of(2001, 2, 10));

        Movie thirdMovie = new Movie();
        thirdMovie.setName("movie3");
        thirdMovie.setDescription("description3");
        thirdMovie.setRelease(LocalDate.of(2002, 2, 10));

        movie = movieRepository.save(movie);
        secondMovie = movieRepository.save(secondMovie);
        thirdMovie = movieRepository.save(thirdMovie);

        MovieFilter filter = new MovieFilter();
        filter.setReleaseDateFrom(LocalDate.of(2000, 1, 10));
        filter.setReleaseDateTo(LocalDate.of(2002, 2, 10));

        assertThat(movieService.getMovies(filter)).extracting("id")
                .containsExactlyInAnyOrder(movie.getId(), secondMovie.getId())
                .doesNotContain(thirdMovie.getId());
    }

    @Test
    public void testGetMoviesWithFilterWithFullFilter() {
        Actor actor = utils.getActorFromDb();

        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setCast(Collections.singletonList(actor));
        movie.setGenres(Stream.of(Genre.ADVENTURE, Genre.COMEDY).collect(Collectors.toSet()));
        movie.setRelease(LocalDate.of(2000, 1, 10));

        Movie secondMovie = new Movie();
        secondMovie.setName("movie");
        secondMovie.setDescription("description2");
        secondMovie.setCast(Collections.singletonList(actor));
        secondMovie.setGenres(Stream.of(Genre.COMEDY, Genre.SCIENCE_FICTION).collect(Collectors.toSet()));
        secondMovie.setRelease(LocalDate.of(2001, 2, 10));

        Movie thirdMovie = new Movie();
        thirdMovie.setName("movie3");
        thirdMovie.setDescription("description3");
        thirdMovie.setCast(Collections.singletonList(actor));
        thirdMovie.setGenres(Stream.of(Genre.HORROR).collect(Collectors.toSet()));
        thirdMovie.setRelease(LocalDate.of(2002, 2, 10));

        movie = movieRepository.save(movie);
        secondMovie = movieRepository.save(secondMovie);
        thirdMovie = movieRepository.save(thirdMovie);

        MovieFilter filter = new MovieFilter();
        filter.setReleaseDateFrom(LocalDate.of(2000, 1, 10));
        filter.setReleaseDateTo(LocalDate.of(2002, 2, 10));
        filter.setName("movie");
        filter.setGenres(Stream.of(Genre.COMEDY).collect(Collectors.toSet()));
        filter.setActorId(actor.getId());

        assertThat(movieService.getMovies(filter)).extracting("id")
                .containsExactlyInAnyOrder(movie.getId(), secondMovie.getId())
                .doesNotContain(thirdMovie.getId());
    }

    @Test
    public void testCalcRating() {
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setGenres(Stream.of(Genre.ADVENTURE, Genre.COMEDY).collect(Collectors.toSet()));
        movie.setRelease(LocalDate.of(2000, 1, 10));

        movie = movieRepository.save(movie);

        Rating rating = new Rating();
        rating.setUser(utils.getUserFromDb());
        rating.setEntityId(movie.getId());
        rating.setRating(3.4);
        ratingRepository.save(rating);

        Rating secondRating = new Rating();
        secondRating.setUser(utils.getUserFromDb());
        secondRating.setEntityId(movie.getId());
        secondRating.setRating(0.5);
        ratingRepository.save(secondRating);

        Assert.assertEquals(1.95d, movieRepository.calcAverageRating(movie.getId()), Double.MIN_VALUE);
    }

    @Test
    public void testUpdateAverageRatingOfMovie() {
        Movie movie = new Movie();
        movie.setName("movie");
        movie.setDescription("description");
        movie.setGenres(Stream.of(Genre.ADVENTURE, Genre.COMEDY).collect(Collectors.toSet()));
        movie.setRelease(LocalDate.of(2000, 1, 10));

        movie = movieRepository.save(movie);
        Double oldAvgRating = movie.getRating();
        Assert.assertNull(oldAvgRating);

        Rating rating = new Rating();
        rating.setUser(utils.getUserFromDb());
        rating.setEntityId(movie.getId());
        rating.setRating(3.4);
        ratingRepository.save(rating);

        Rating secondRating = new Rating();
        secondRating.setUser(utils.getUserFromDb());
        secondRating.setEntityId(movie.getId());
        secondRating.setRating(0.5);
        ratingRepository.save(secondRating);

        movieService.updateAverageRatingOfMovie(movie.getId());
        movie = movieRepository.findById(movie.getId()).get();
        Assert.assertEquals(1.95d, movie.getRating(), Double.MIN_VALUE);
    }
}