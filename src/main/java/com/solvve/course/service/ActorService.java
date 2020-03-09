package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Person;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorPutDto;
import com.solvve.course.repository.ActorRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public ActorExtendedReadDto getActor(UUID id) {
        Actor actorFromDb = repositoryHelper.getEntityRequired(Actor.class, id);

        return translationService.toExtendedReadDto(actorFromDb);
    }

    @Transactional
    public ActorExtendedReadDto addActor(ActorCreateDto actorCreateDto) {
        Actor actor = translationService.toEntity(actorCreateDto);
        actor = actorRepository.save(actor);

        return translationService.toExtendedReadDto(actor);
    }

    public ActorExtendedReadDto patchActor(UUID id, ActorPatchDto actorPatchDto) {
        Actor actor = repositoryHelper.getEntityRequired(Actor.class, id);
        if (nonNull(actorPatchDto.getPersonId())) {
            actor.setPerson(repositoryHelper.getReferenceIfExist(Person.class, actorPatchDto.getPersonId()));
        }
        Actor patchedActor = actorRepository.save(actor);

        return translationService.toExtendedReadDto(patchedActor);
    }

    public void deleteActor(UUID id) {
        actorRepository.delete(repositoryHelper.getEntityRequired(Actor.class, id));
    }

    public ActorExtendedReadDto updateActor(UUID id, ActorPutDto actorPutDto) {
        Actor actorFromDb = repositoryHelper.getEntityRequired(Actor.class, id);
        actorFromDb.setPerson(repositoryHelper.getReferenceIfExist(Person.class, actorPutDto.getPersonId()));

        return translationService.toExtendedReadDto(actorFromDb);
    }
}
