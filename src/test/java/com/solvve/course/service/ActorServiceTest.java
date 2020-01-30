package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Character;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.Person;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.ActorRepository;
import com.solvve.course.repository.MovieRepository;
import com.solvve.course.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql(statements = "delete from actor", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//@Sql(statements = "delete from movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//@Sql(statements = "delete from person", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ActorServiceTest {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private ActorService actorService;

    @Test
    @Transactional
    public void testGetActor() {
        Actor actor = this.getActorFromDb();

        ActorReadDto actorReadDto = actorService.getActor(actor.getId());
        assertThat(actorReadDto).isEqualToComparingFieldByField(actor);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieByWrongId() {
        actorService.getActor(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testCreateActor() {
        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPerson(translationService.toReadDto(this.getPersonFromDb()));
        actorCreateDto.setMovies(Collections.singletonList(translationService.toReadDto(this.getMovieFromDb())));

        ActorReadDto actorReadDto = actorService.addActor(actorCreateDto);

        assertThat(actorCreateDto).isEqualToComparingFieldByField(actorReadDto);
        assertNotNull(actorReadDto.getId());

        Actor actorFromDb = actorRepository.findById(actorReadDto.getId()).get();
        assertThat(actorReadDto).isEqualToComparingFieldByField(actorFromDb);
    }

//    @Test
//    @Transactional
//    public void testPatchMovie() {
//        MoviePatchDto moviePatchDto = new MoviePatchDto();
//        moviePatchDto.setName("Epic");
//        moviePatchDto.setDescription("test Description");
//        moviePatchDto.setGenres(new HashSet<>(Arrays.asList(Genre.COMEDY, Genre.WESTERN)));
//
//        MovieCreateDto movieCreateDto = createMovieCreateDto();
//        MovieReadDto movieFromDb = movieService.addMovie(movieCreateDto);
//
//        MovieReadDto patchedMovie = movieService.patchMovie(movieFromDb.getId(), moviePatchDto);
//
//        assertThat(moviePatchDto).isEqualToIgnoringGivenFields(patchedMovie,
//                "characters", "cast", "stars");
//    }
//
//    @Test
//    @Transactional
//    public void testEmptyPatchMovie() {
//        MoviePatchDto moviePatchDto = new MoviePatchDto();
//
//        MovieCreateDto movieCreateDto = createMovieCreateDto();
//        MovieReadDto movieBeforePatch = movieService.addMovie(movieCreateDto);
//
//        MovieReadDto movieAfterPatch = movieService.patchMovie(movieBeforePatch.getId(), moviePatchDto);
//        assertNotNull(movieAfterPatch.getDescription());
//        assertNotNull(movieAfterPatch.getName());
//        assertNotNull(movieAfterPatch.getGenres());
//
//        assertThat(movieBeforePatch).isEqualToComparingFieldByField(movieAfterPatch);
//    }

    @Test
    public void testDeleteActor() {
        Actor actor = this.getActorFromDb();

        actorService.deleteActor(actor.getId());

        assertFalse(actorRepository.existsById(actor.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        actorService.deleteActor(UUID.randomUUID());
    }

    private Actor getActorFromDb() {
        Movie movieFromDb = this.getMovieFromDb();
        Actor actor = new Actor();
        actor.setMovies(Collections.singletonList(movieFromDb));
        actor.setPerson(this.getPersonFromDb());
        actor.setCharacters(Collections.singletonList(this.getCharacterFromDb(actor, movieFromDb)));

        return actorRepository.save(actor);
    }

    private Movie getMovieFromDb() {
        Movie movie = new Movie();
        movie.setName("Shattered island");
        movie.setDescription("cool film");
        movie.setGenres(new HashSet<>(Arrays.asList(Genre.DRAMA, Genre.ADVENTURE)));

        return movieRepository.save(movie);
    }

    private Person getPersonFromDb() {
        Person person = new Person();
        person.setName("Test");

        return personRepository.save(person);
    }

    private Character getCharacterFromDb(Actor actor, Movie movieFromDb) {
        Character character = new Character();
        character.setName("Test Character");
        character.setActor(actor);
        character.setMovie(movieFromDb);

        return character;
    }
}
