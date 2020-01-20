package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.dto.ActorCreateDto;
import com.solvve.course.dto.ActorReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public ActorReadDto getActor(UUID id) {
        Actor actorFromDb = actorRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Actor.class, id));
        return mapToReadDto(actorFromDb);
    }

    public ActorReadDto addActor(ActorCreateDto actorCreateDto) {
        Actor actor = new Actor();
        actor.setPerson(actorCreateDto.getPerson());
        actor.setCharacters(actorCreateDto.getCharacters());
        actor.setMovies(actorCreateDto.getMovies());
        actor.setMoviesAsStar(actor.getMoviesAsStar());

        actor = actorRepository.save(actor);
        return mapToReadDto(actor);
    }

    private ActorReadDto mapToReadDto(Actor actor) {
        ActorReadDto dto = new ActorReadDto();
        dto.setId(actor.getId());
        dto.setCharacters(actor.getCharacters());
        dto.setMovies(actor.getMovies());
        dto.setMoviesAsStar(actor.getMoviesAsStar());
        dto.setPerson(actor.getPerson());

        return dto;
    }
}
