package com.solvve.course.service;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.*;
import com.solvve.course.dto.PageResult;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorPutDto;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterPatchDto;
import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.correction.CorrectionPatchDto;
import com.solvve.course.dto.movie.MoviePatchDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationPatchDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.repository.RepositoryHelper;
import org.bitbucket.brunneng.ot.Configuration;
import org.bitbucket.brunneng.ot.ObjectTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public <T> List<T> translateList(List<?> objects, Class<T> targetClass) {
        return objects.stream()
                .map(o -> translate(o, targetClass))
                .collect(Collectors.toList());
    }

    public void map(Object patchDto, Object entity) {
        objectTranslator.mapBean(patchDto, entity);
    }

    private Configuration createConfiguration() {
        Configuration configuration = new Configuration();
        configureForUser(configuration);
        configureForAbstractEntity(configuration);
        configureForPublication(configuration);
        configureForCharacter(configuration);
        configureForCorrection(configuration);
        configureForActor(configuration);
        configureForPrincipal(configuration);
        configureForPerson(configuration);
        configureForMovie(configuration);

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
        t = c.beanOfClass(UserPatchDto.class).translationTo(User.class);
        t.srcProperty("principalId").translatesTo("principal.id");

        c.beanOfClass(UserPatchDto.class).translationTo(User.class).mapOnlyNotNullProperties();
    }

    private void configureForPublication(Configuration c) {
        Configuration.Translation t = c.beanOfClass(PublicationCreateDto.class).translationTo(Publication.class);
        t.srcProperty("managerId").translatesTo("manager.id");
        t = c.beanOfClass(PublicationPatchDto.class).translationTo(Publication.class);
        t.srcProperty("managerId").translatesTo("manager.id");

        c.beanOfClass(PublicationPatchDto.class).translationTo(Publication.class).mapOnlyNotNullProperties();
    }

    private void configureForCharacter(Configuration c) {
        Configuration.Translation t = c.beanOfClass(CharacterCreateDto.class).translationTo(Character.class);
        t.srcProperty("movieId").translatesTo("movie.id");
        t.srcProperty("actorId").translatesTo("actor.id");
        t = c.beanOfClass(CharacterPatchDto.class).translationTo(Character.class);
        t.srcProperty("movieId").translatesTo("movie.id");
        t.srcProperty("actorId").translatesTo("actor.id");

        c.beanOfClass(CharacterPatchDto.class).translationTo(Character.class).mapOnlyNotNullProperties();
    }

    private void configureForActor(Configuration c) {
        Configuration.Translation t = c.beanOfClass(ActorCreateDto.class).translationTo(Actor.class);
        t.srcProperty("personId").translatesTo("person.id");
        t = c.beanOfClass(ActorPatchDto.class).translationTo(Actor.class);
        t.srcProperty("personId").translatesTo("person.id");
        t = c.beanOfClass(ActorPutDto.class).translationTo(Actor.class);
        t.srcProperty("personId").translatesTo("person.id");

        c.beanOfClass(ActorPatchDto.class).translationTo(Actor.class).mapOnlyNotNullProperties();
    }

    private void configureForCorrection(Configuration c) {
        Configuration.Translation t = c.beanOfClass(CorrectionCreateDto.class).translationTo(Correction.class);
        t.srcProperty("userId").translatesTo("user.id");
        t.srcProperty("publicationId").translatesTo("publication.id");
        t = c.beanOfClass(CorrectionPatchDto.class).translationTo(Correction.class);
        t.srcProperty("userId").translatesTo("user.id");
        t.srcProperty("publicationId").translatesTo("publication.id");

        c.beanOfClass(CorrectionPatchDto.class).translationTo(Correction.class).mapOnlyNotNullProperties();
    }

    private void configureForPrincipal(Configuration c) {
        c.beanOfClass(PrincipalPatchDto.class).translationTo(Principal.class).mapOnlyNotNullProperties();
    }

    private void configureForPerson(Configuration c) {
        c.beanOfClass(PersonPatchDto.class).translationTo(Person.class).mapOnlyNotNullProperties();
    }

    private void configureForMovie(Configuration c) {
        c.beanOfClass(MoviePatchDto.class).translationTo(Movie.class).mapOnlyNotNullProperties();
    }

    public <E, T> PageResult<T> toPageResult(Page<E> page, Class<T> targetClass) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPage(page.getNumber());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotalElements(page.getTotalElements());
        pageResult.setTotalPages(page.getTotalPages());
        pageResult.setData(page.getContent().stream().map(e -> translate(e, targetClass)).collect(Collectors.toList()));

        return pageResult;
    }
}