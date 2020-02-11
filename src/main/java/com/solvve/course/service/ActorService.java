package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
import com.solvve.course.dto.actor.ActorPutDto;
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

    public ActorExtendedReadDto getActor(UUID id) {
        Actor actorFromDb = getActorRequired(id);
        return translationService.toExtendedReadDto(actorFromDb);
    }

    public ActorExtendedReadDto addActor(ActorCreateDto actorCreateDto) {
        Actor actor = translationService.toEntity(actorCreateDto);
        actor = actorRepository.save(actor);
        return translationService.toExtendedReadDto(actor);
    }

    public ActorExtendedReadDto patchActor(UUID id, ActorPatchDto actorPatchDto) {
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

        return translationService.toExtendedReadDto(patchedActor);
    }

    public void deleteActor(UUID id) {
        actorRepository.delete(getActorRequired(id));
    }

    public ActorExtendedReadDto putActor(UUID id, ActorPutDto actorPutDto) {
        Actor actorFromDb = this.getActorRequired(id);
        actorFromDb.setPerson(translationService.toEntity(actorPutDto.getPerson()));
        actorFromDb.setMovies(actorPutDto.getMovies()
                .stream()
                .map(translationService::toEntity)
                .collect(Collectors.toList()));
        actorFromDb.setMoviesAsStar(actorPutDto.getMoviesAsStar()
                .stream()
                .map(translationService::toEntity)
                .collect(Collectors.toList()));
        actorFromDb.setCharacters(actorPutDto.getCharacters()
                .stream()
                .map(translationService::toEntity)
                .collect(Collectors.toList()));
        actorFromDb = actorRepository.save(actorFromDb);

        return translationService.toExtendedReadDto(actorFromDb);
    }

    private Actor getActorRequired(UUID id) {
        return actorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Actor.class, id));
    }
}
