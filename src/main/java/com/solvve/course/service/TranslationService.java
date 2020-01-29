package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Character;
import com.solvve.course.domain.Movie;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TranslationService {

    public MovieReadDto toReadDto(Movie movie) {
        MovieReadDto dto = new MovieReadDto();
        dto.setId(movie.getId());
        dto.setCast(this.toReadDto(movie.getCast()));
        dto.setCharacters(movie.getCharacters()
                .stream()
                .map(this::toReadDto)
                .collect(Collectors.toList()));
        dto.setDescription(movie.getDescription());
        dto.setGenres(movie.getGenres());
        dto.setStars(this.toReadDto(movie.getStars()));

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

    public ActorReadDto toReadDto(Actor actor) {
        ActorReadDto dto = new ActorReadDto();
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
}