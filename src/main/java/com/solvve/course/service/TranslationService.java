package com.solvve.course.service;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.*;
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
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    @Autowired
    private RepositoryHelper repositoryHelper;

    public MovieReadDto toReadDto(Movie movie) {
        MovieReadDto dto = new MovieReadDto();
        dto.setId(movie.getId());
        dto.setName(movie.getName());
        dto.setRelease(movie.getRelease());
        dto.setDescription(movie.getDescription());
        dto.setCreatedAt(movie.getCreatedAt());
        dto.setUpdatedAt(movie.getUpdatedAt());

        return dto;
    }

    public CharacterReadDto toReadDto(Character character) {
        CharacterReadDto dto = new CharacterReadDto();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setMovie(this.toReadDto(character.getMovie()));
        dto.setActor(this.toReadDto(character.getActor()));
        dto.setCreatedAt(character.getCreatedAt());
        dto.setUpdatedAt(character.getUpdatedAt());

        return dto;
    }

    public ActorReadDto toReadDto(Actor actor) {
        ActorReadDto dto = new ActorReadDto();
        dto.setId(actor.getId());
        dto.setPersonId(actor.getPerson().getId());

        return dto;
    }

    public PersonReadDto toReadDto(Person person) {
        PersonReadDto dto = new PersonReadDto();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setCreatedAt(person.getCreatedAt());
        dto.setUpdatedAt(person.getUpdatedAt());

        return dto;
    }

    public UserReadDto toReadDto(User user) {
        UserReadDto dto = new UserReadDto();
        dto.setId(user.getId());
        dto.setBlockedReview(user.getBlockedReview());
        dto.setPrincipal(this.toReadDto(user.getPrincipal()));
        dto.setTrustLevel(user.getTrustLevel());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    public PrincipalReadDto toReadDto(Principal principal) {
        PrincipalReadDto dto = new PrincipalReadDto();
        dto.setId(principal.getId());
        dto.setBlocked(principal.getBlocked());
        dto.setEmail(principal.getEmail());
        dto.setName(principal.getName());
        dto.setRole(principal.getRole());
        dto.setCreatedAt(principal.getCreatedAt());
        dto.setUpdatedAt(principal.getUpdatedAt());

        return dto;
    }

    public PublicationReadDto toReadDto(Publication publication) {
        PublicationReadDto dto = new PublicationReadDto();
        dto.setId(publication.getId());
        dto.setCreatedAt(publication.getCreatedAt());
        dto.setUpdatedAt(publication.getUpdatedAt());
        dto.setManager(this.toReadDto(publication.getManager()));
        dto.setTitle(publication.getTitle());
        dto.setContent(publication.getContent());

        return dto;
    }

    public ActorExtendedReadDto toExtendedReadDto(Actor actor) {
        ActorExtendedReadDto dto = new ActorExtendedReadDto();
        dto.setId(actor.getId());
        dto.setPerson(this.toReadDto(actor.getPerson()));
        dto.setCreatedAt(actor.getCreatedAt());
        dto.setUpdatedAt(actor.getUpdatedAt());
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
        character.setMovie(repositoryHelper.getReferenceIfExist(Movie.class, dto.getMovieId()));
        character.setActor(repositoryHelper.getReferenceIfExist(Actor.class, dto.getActorId()));

        return character;
    }

    public Actor toEntity(ActorCreateDto dto) {
        Actor actor = new Actor();
        actor.setPerson(repositoryHelper.getReferenceIfExist(Person.class, dto.getPersonId()));

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
        user.setPrincipal(repositoryHelper.getReferenceIfExist(Principal.class, dto.getPrincipalId()));
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

    public Publication toEntity(PublicationCreateDto dto) {
        Publication publication = new Publication();
        publication.setManager(repositoryHelper.getReferenceIfExist(Principal.class, dto.getManagerId()));
        publication.setTitle(dto.getTitle());
        publication.setContent(dto.getContent());

        return publication;
    }
}