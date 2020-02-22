package com.solvve.course.util;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.*;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorPutDto;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
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

    public Actor getActorFromDb() {
        Actor actor = new Actor();
        actor.setPerson(this.getPersonFromDb());

        return actorRepository.save(actor);
    }

    public Movie getMovieFromDb() {
        Movie movie = new Movie();
        movie.setName("Test film");
        movie.setDescription("test description");
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

    public Character getCharacterFromDb() {
        Character character = new Character();
        character.setName("Test Character");
        character.setActor(this.getActorFromDb());
        character.setMovie(this.getMovieFromDb());

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

    public PersonCreateDto createPersonCreateDto() {
        PersonCreateDto personCreateDto = new PersonCreateDto();
        personCreateDto.setName("create");

        return personCreateDto;
    }

    public CharacterCreateDto createCharacterCreateDto() {
        CharacterCreateDto characterCreateDto = new CharacterCreateDto();
        characterCreateDto.setName("iao");
        characterCreateDto.setActorId(this.getActorFromDb().getId());
        characterCreateDto.setMovieId(this.getMovieFromDb().getId());

        return characterCreateDto;
    }

    public MovieCreateDto createMovieCreateDto() {
        MovieCreateDto movieCreateDto = new MovieCreateDto();
        movieCreateDto.setName("Shattered island");
        movieCreateDto.setDescription("cool film");

        return movieCreateDto;
    }

    public UserCreateDto createUserCreateDto() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setBlockedReview(true);
        userCreateDto.setPrincipalId(getPrincipalFromDb().getId());
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

        return movieReadDto;
    }

    public PersonReadDto createPersonReadDto() {
        PersonReadDto personReadDto = new PersonReadDto();
        personReadDto.setId(UUID.randomUUID());
        personReadDto.setName("Read Dto");

        return personReadDto;
    }

    public PersonPatchDto createPersonPatchDto() {
        PersonPatchDto personPatchDto = new PersonPatchDto();
        personPatchDto.setName("new Name");

        return personPatchDto;
    }

    public ActorExtendedReadDto createActorExtendedReadDto() {
        ActorExtendedReadDto actorExtendedReadDto = new ActorExtendedReadDto();
        actorExtendedReadDto.setId(UUID.randomUUID());
        actorExtendedReadDto.setMovies(Collections.singletonList(this.createMovieReadDto()));
        actorExtendedReadDto.setPerson(this.createPersonReadDto());

        return actorExtendedReadDto;
    }

    public ActorPatchDto createActorPatchDto() {
        ActorPatchDto actorPatchDto = new ActorPatchDto();
        actorPatchDto.setPersonId(this.createPersonReadDto().getId());

        return actorPatchDto;
    }

    public ActorPutDto createActorPutDto() {
        ActorPutDto actorPutDto = new ActorPutDto();
        actorPutDto.setPersonId(this.createPersonReadDto().getId());

        return actorPutDto;
    }

    public ActorCreateDto createActorCreateDto() {
        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPersonId(this.createPersonReadDto().getId());

        return actorCreateDto;
    }
}