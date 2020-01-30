package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private TranslationService translationService;

    public ActorReadDto getActor(UUID id) {
        Actor actorFromDb = getActorRequired(id);
        return translationService.toReadDto(actorFromDb);
    }

    public ActorReadDto addActor(ActorCreateDto actorCreateDto) {
        Actor actor = translationService.toEntity(actorCreateDto);
        actor = actorRepository.save(actor);
        return translationService.toReadDto(actor);
    }

    public void deleteActor(UUID id) {
        actorRepository.delete(getActorRequired(id));
    }

    private Actor getActorRequired(UUID id) {
        return actorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Actor.class, id));
    }
}
