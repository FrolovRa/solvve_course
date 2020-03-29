package com.solvve.course.service;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.*;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.movie.MovieCreateDto;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.repository.RepositoryHelper;
import org.bitbucket.brunneng.ot.Configuration;
import org.bitbucket.brunneng.ot.ObjectTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TranslationService {

    @Autowired
    private RepositoryHelper repositoryHelper;

    private ObjectTranslator objectTranslator;

    public TranslationService() {
        objectTranslator = new ObjectTranslator(createConfiguration());
    }

    private Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configureForUser(configuration);
        configureForPrincipal(configuration);

        return configuration;
    }

    private void configureForUser(Configuration c) {
        c.beanOfClass(User.class).setIdentifierProperty("id");
        c.beanOfClass(User.class).setBeanFinder(
            (beanClass, id) -> repositoryHelper.getReferenceIfExist(beanClass, (UUID) id));

        Configuration.Translation t = c.beanOfClass(UserCreateDto.class).translationTo(User.class);
        t.srcProperty("principalId").translatesTo("principal.id");
    }

    private void configureForPrincipal(Configuration c) {
        c.beanOfClass(Principal.class).setIdentifierProperty("id");
        c.beanOfClass(Principal.class).setBeanFinder(
            (beanClass, id) -> repositoryHelper.getReferenceIfExist(beanClass, (UUID) id));
    }

    public <T> T translate(Object srcObject, Class<T> targetClass) {
        return objectTranslator.translate(srcObject, targetClass);
    }

    public Movie toEntity(MovieCreateDto dto) {

        return objectTranslator.translate(dto, Movie.class);
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

        return objectTranslator.translate(dto, Person.class);
    }

    public User toEntity(UserCreateDto dto) {

        return objectTranslator.translate(dto, User.class);
    }

    public Principal toEntity(PrincipalCreateDto dto) {

        return objectTranslator.translate(dto, Principal.class);
    }

    public Publication toEntity(PublicationCreateDto dto) {
        Publication publication = new Publication();
        publication.setManager(repositoryHelper.getReferenceIfExist(Principal.class, dto.getManagerId()));
        publication.setContent(dto.getContent());

        return publication;
    }

    public Correction toEntity(CorrectionCreateDto dto) {
        Correction correction = new Correction();
        correction.setUser(repositoryHelper.getReferenceIfExist(User.class, dto.getUserId()));
        correction.setPublication(repositoryHelper.getReferenceIfExist(Publication.class, dto.getPublicationId()));
        correction.setStartIndex(dto.getStartIndex());
        correction.setSelectedText(dto.getSelectedText());
        correction.setProposedText(dto.getProposedText());

        return correction;
    }
}