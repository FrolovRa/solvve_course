package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

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

    public ActorReadDto patchActor(UUID id, ActorPatchDto actorPatchDto) {
        Actor actor = this.getActorRequired(id);

        if (nonNull(actorPatchDto.getPerson())) {
            actor.setPerson(translationService.toEntity(actorPatchDto.getPerson()));
        }

        if (nonNull(actorPatchDto.getMovies())) {
            actor.setMovies(actorPatchDto.getMovies()
                    .stream()
                    .map(translationService::toEntity)
                    .collect(Collectors.toList()));
        }

        if (nonNull(actorPatchDto.getCharacters())) {
            actor.setCharacters(actorPatchDto.getCharacters()
                    .stream()
                    .map(translationService::toEntity)
                    .collect(Collectors.toList()));
        }

        if (nonNull(actorPatchDto.getMoviesAsStar())) {
            actor.setMoviesAsStar(actorPatchDto.getMoviesAsStar()
                    .stream()
                    .map(translationService::toEntity)
                    .collect(Collectors.toList()));
        }

        Actor patchedActor = actorRepository.save(actor);

        return translationService.toReadDto(patchedActor);
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
