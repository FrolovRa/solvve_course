package com.solvve.course.util;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Character;
import com.solvve.course.domain.Movie;
import com.solvve.course.domain.Person;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.repository.ActorRepository;
import com.solvve.course.repository.CharacterRepository;
import com.solvve.course.repository.MovieRepository;
import com.solvve.course.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
public class TestUtils {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CharacterRepository characterRepository;

    public Actor getActorFromDb() {
        Movie movieFromDb = this.getMovieFromDb();
        Actor actor = new Actor();
        actor.setMovies(Collections.singletonList(movieFromDb));
        actor.setPerson(this.getPersonFromDb());

        return actorRepository.save(actor);
    }

    public Movie getMovieFromDb() {
        Movie movie = new Movie();
        movie.setName("Test film");
        movie.setDescription("test description");
        movie.setGenres(new HashSet<>(Arrays.asList(Genre.DRAMA, Genre.ADVENTURE)));
        movie.setRelease(LocalDate.now());

        return movieRepository.save(movie);
    }

    public Person getPersonFromDb() {
        Person person = new Person();
        person.setName("Test");

        return personRepository.save(person);
    }

    public Character getCharacterFromDb(Actor actor, Movie movieFromDb) {
        Character character = new Character();
        character.setName("Test Character");
        character.setActor(actor);
        character.setMovie(movieFromDb);

        return characterRepository.save(character);
    }
}