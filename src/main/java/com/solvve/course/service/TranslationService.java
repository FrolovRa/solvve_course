package com.solvve.course.service;

import com.solvve.course.domain.*;
import com.solvve.course.domain.Character;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserReadDto;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    @PersistenceContext
    private EntityManager entityManager;

    public MovieReadDto toReadDto(Movie movie) {
        MovieReadDto dto = new MovieReadDto();
        dto.setId(movie.getId());
        dto.setName(movie.getName());
        dto.setRelease(movie.getRelease());
        dto.setDescription(movie.getDescription());

        return dto;
    }

    public CharacterReadDto toReadDto(Character character) {
        CharacterReadDto dto = new CharacterReadDto();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setMovie(this.toReadDto(character.getMovie()));
        dto.setActor(this.toReadDto(character.getActor()));

        return dto;
    }

    public ActorExtendedReadDto toExtendedReadDto(Actor actor) {
        ActorExtendedReadDto dto = new ActorExtendedReadDto();
        dto.setId(actor.getId());
        dto.setCharacters(actor.getCharacters()
                .stream()
                .map(this::toReadDto)
                .collect(Collectors.toList()));
        dto.setMovies(actor.getMovies()
                .stream()
                .map(this::toReadDto)
                .collect(Collectors.toList()));
        dto.setMoviesAsStar(actor.getMoviesAsStar()
                .stream()
                .map(this::toReadDto)
                .collect(Collectors.toList()));
        dto.setPerson(this.toReadDto(actor.getPerson()));

        return dto;
    }

    public ActorReadDto toReadDto(Actor actor) {
        ActorReadDto dto = new ActorReadDto();
        dto.setId(actor.getId());
        dto.setPerson(this.toReadDto(actor.getPerson()));

        return dto;
    }

    public PersonReadDto toReadDto(Person person) {
        PersonReadDto dto = new PersonReadDto();
        dto.setId(person.getId());
        dto.setName(person.getName());

        return dto;
    }

    public UserReadDto toReadDto(User user) {
        UserReadDto dto = new UserReadDto();
        dto.setId(user.getId());
        dto.setBlockedReview(user.isBlockedReview());
        dto.setPrincipal(this.toReadDto(user.getPrincipal()));
        dto.setTrustLevel(user.getTrustLevel());

        return dto;
    }

    public PrincipalReadDto toReadDto(Principal principal) {
        PrincipalReadDto dto = new PrincipalReadDto();
        dto.setId(principal.getId());
        dto.setBlocked(principal.isBlocked());
        dto.setEmail(principal.getEmail());
        dto.setName(principal.getName());
        dto.setRole(principal.getRole());

        return dto;
    }

    public Movie toEntity(MovieCreateDto dto) {
        Movie movie = new Movie();
        movie.setName(dto.getName());
        movie.setRelease(dto.getRelease());
        movie.setDescription(dto.getDescription());

        return movie;
    }

    public Character toEntity(CharacterCreateDto dto) {
        Character character = new Character();
        character.setName(dto.getName());
        character.setMovie(this.getReference(Movie.class, dto.getMovieId()));
        character.setActor(this.getReference(Actor.class, dto.getActorId()));

        return character;
    }

    public Actor toEntity(ActorCreateDto dto) {
        Actor actor = new Actor();
        actor.setPerson(this.getReference(Person.class, dto.getPersonId()));

        return actor;
    }

    public Person toEntity(PersonCreateDto dto) {
        Person person = new Person();
        person.setName(dto.getName());

        return person;
    }

    public User toEntity(UserCreateDto dto) {
        User user = new User();
        user.setBlockedReview(dto.getBlockedReview());
        user.setPrincipal(this.getReference(Principal.class, dto.getPrincipalId()));
        user.setTrustLevel(dto.getTrustLevel());

        return user;
    }

    public Principal toEntity(PrincipalCreateDto dto) {
        Principal principal = new Principal();
        principal.setBlocked(dto.getBlocked());
        principal.setEmail(dto.getEmail());
        principal.setName(dto.getName());
        principal.setRole(dto.getRole());

        return principal;
    }

    public <E> E getReference(Class<E> entityClass, UUID id) {
        return entityManager.getReference(entityClass, id);
    }
}