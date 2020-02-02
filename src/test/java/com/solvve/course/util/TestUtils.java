package com.solvve.course.util;

import com.solvve.course.domain.*;
import com.solvve.course.domain.Character;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.repository.*;
import com.solvve.course.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrincipalRepository principalRepository;
    @Autowired
    private TranslationService translationService;

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

    public User getUserFromDb() {
        User user = new User();
        user.setTrustLevel(1);
        user.setBlockedReview(true);
        user.setPrincipal(this.getPrincipalFromDb());

        return userRepository.save(user);
    }

    public Principal getPrincipalFromDb() {
        Principal principal = new Principal();
        principal.setName("Principal");
        principal.setBlocked(false);
        principal.setEmail("principal@mail.com");
        principal.setRole(Role.USER);

        return principalRepository.save(principal);
    }


    public MovieCreateDto createMovieCreateDto() {
        MovieCreateDto movieCreateDto = new MovieCreateDto();
        movieCreateDto.setName("Shattered island");
        movieCreateDto.setDescription("cool film");
        movieCreateDto.setGenres(new HashSet<>(Arrays.asList(Genre.DRAMA, Genre.ADVENTURE)));

        return movieCreateDto;
    }

    public UserCreateDto createUserCreateDto() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setBlockedReview(true);
        userCreateDto.setPrincipal(translationService.toReadDto(getPrincipalFromDb()));
        userCreateDto.setTrustLevel(1);

        return userCreateDto;
    }

    public PrincipalCreateDto createPrincipalCreateDto() {
        PrincipalCreateDto principalCreateDto = new PrincipalCreateDto();
        principalCreateDto.setName("Test");
        principalCreateDto.setRole(Role.USER);
        principalCreateDto.setBlocked(false);
        principalCreateDto.setBlocked(false);

        return principalCreateDto;
    }

    public MovieReadDto createMovieReadDto() {
        MovieReadDto movieReadDto = new MovieReadDto();
        movieReadDto.setId(UUID.randomUUID());
        movieReadDto.setName("Mr.Nobody");
        movieReadDto.setDescription("cool film");
        movieReadDto.setGenres(Collections.singleton(Genre.COMEDY));

        return movieReadDto;
    }
}