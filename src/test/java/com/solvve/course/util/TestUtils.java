package com.solvve.course.util;

import com.solvve.course.domain.*;
import com.solvve.course.domain.Character;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonCreateDto;
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

    public static final UUID TEST_MOVIE_ONE = UUID.fromString("e9e0c490-5df8-45c4-9174-a4ad116868be");
    public static final UUID TEST_MOVIE_TWO = UUID.fromString("6606d4c9-4083-498c-bf4f-cee1154908c8");
    public static final UUID TEST_MOVIE_THREE = UUID.fromString("25693267-2e78-4b02-88d5-67f53ceace78");
    public static final UUID TEST_ACTOR_ONE = UUID.fromString("09b6d980-b8e1-4b10-a78c-d9ac6003f9a0");
    public static final UUID TEST_ACTOR_TWO = UUID.fromString("c37a24bc-02d6-4195-9286-b0b38e57ebe9");
    public static final UUID TEST_ACTOR_THREE = UUID.fromString("54b61c63-a8a3-4b41-8bef-9239fe354dc5");
    public static final UUID TEST_CHARACTER_ONE = UUID.fromString("21cbc086-38fe-4028-b703-eb64efcdd81b");
    public static final UUID TEST_CHARACTER_TWO = UUID.fromString("7f29c3fd-2e6a-4a9c-ab87-49698fb61a5e");
    public static final UUID TEST_CHARACTER_THREE = UUID.fromString("83f6686f-24d7-457f-9fef-876dc1f34c53");
    public static final UUID TEST_PERSON_ONE = UUID.fromString("49f801c7-de7e-42bb-9725-863d94432808");
    public static final UUID TEST_PERSON_TWO = UUID.fromString("8dfe62f9-e0f5-4abf-b1e8-608094174e6b");
    public static final UUID TEST_PERSON_THREE = UUID.fromString("11be7710-5bc8-42a5-b5ff-40ae53080014");
    public static final UUID TEST_PRINCIPAL_ONE = UUID.fromString("efa58f40-b832-4cab-9842-863d94432808");
    public static final UUID TEST_PRINCIPAL_TWO = UUID.fromString("0d09a686-e3d8-4150-9579-608094174e6b");
    public static final UUID TEST_PRINCIPAL_THREE = UUID.fromString("200ee407-f074-4a14-8848-40ae53080014");
    public static final UUID TEST_USER_ONE = UUID.fromString("49f801c7-de7e-42bb-9725-7944cf567fcf");
    public static final UUID TEST_USER_TWO = UUID.fromString("8dfe62f9-e0f5-4abf-b1e8-d4cb02124d67");
    public static final UUID TEST_USER_THREE = UUID.fromString("11be7710-5bc8-42a5-b5ff-f513a4beea4a");
    public static final UUID TEST_DIRECTOR_ONE = UUID.fromString("827063b5-9f9c-4adf-9084-e01954a94cba");
    public static final UUID TEST_DIRECTOR_TWO = UUID.fromString("40aa3db6-dfe6-488f-be20-43796eb589ed");
    public static final UUID TEST_DIRECTOR_THREE = UUID.fromString("97875062-4567-45d9-80db-06f7cfb6d8ee");

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
        characterCreateDto.setActor(translationService.toReadDto(this.getActorFromDb()));
        characterCreateDto.setMovie(translationService.toReadDto(this.getMovieFromDb()));

        return characterCreateDto;
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

    public ActorCreateDto createActorCreateDto() {
        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPerson(translationService.toReadDto(this.getPersonFromDb()));
        actorCreateDto.setMovies(Collections.singletonList(translationService.toReadDto(this.getMovieFromDb())));

        return actorCreateDto;
    }
}