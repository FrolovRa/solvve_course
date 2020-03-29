package com.solvve.course.service;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.*;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.correction.CorrectionCreateDto;
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

    public <T> T translate(Object srcObject, Class<T> targetClass) {
        return objectTranslator.translate(srcObject, targetClass);
    }

    private Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configureForUser(configuration);
        configureForAbstractEntity(configuration);
        configureForPublication(configuration);
        configureForCharacter(configuration);
        configureForCorrection(configuration);
        configureForActor(configuration);

        return configuration;
    }

    private void configureForAbstractEntity(Configuration c) {
        c.beanOfClass(AbstractEntity.class).setIdentifierProperty("id");
        c.beanOfClass(AbstractEntity.class).setBeanFinder(
                (beanClass, id) -> repositoryHelper.getReferenceIfExist(beanClass, (UUID) id));
    }

    private void configureForUser(Configuration c) {
        Configuration.Translation t = c.beanOfClass(UserCreateDto.class).translationTo(User.class);
        t.srcProperty("principalId").translatesTo("principal.id");
    }

    private void configureForPublication(Configuration c) {
        Configuration.Translation t = c.beanOfClass(PublicationCreateDto.class).translationTo(Publication.class);
        t.srcProperty("managerId").translatesTo("manager.id");
    }

    private void configureForCharacter(Configuration c) {
        Configuration.Translation t = c.beanOfClass(CharacterCreateDto.class).translationTo(Character.class);
        t.srcProperty("movieId").translatesTo("movie.id");
        t.srcProperty("actorId").translatesTo("actor.id");
    }

    private void configureForActor(Configuration c) {
        Configuration.Translation t = c.beanOfClass(ActorCreateDto.class).translationTo(Actor.class);
        t.srcProperty("personId").translatesTo("person.id");
    }

    private void configureForCorrection(Configuration c) {
        Configuration.Translation t = c.beanOfClass(CorrectionCreateDto.class).translationTo(Correction.class);
        t.srcProperty("userId").translatesTo("user.id");
        t.srcProperty("publicationId").translatesTo("publication.id");
    }
}