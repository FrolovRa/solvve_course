package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Movie;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterPatchDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.domain.Character;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private TranslationService translationService;

    public CharacterReadDto getCharacter(UUID id) {
        Character characterFromDb = this.getCharacterRequired(id);

        return translationService.toReadDto(characterFromDb);
    }

    public CharacterReadDto addCharacter(CharacterCreateDto movieCreateDto) {
        Character character = translationService.toEntity(movieCreateDto);
        character = characterRepository.save(character);

        return translationService.toReadDto(character);
    }

    public CharacterReadDto patchPerson(UUID id, CharacterPatchDto characterPatchDto) {
        Character character = this.getCharacterRequired(id);
        if (nonNull(characterPatchDto.getName())) {
            character.setName(characterPatchDto.getName());
        }
        if (nonNull(characterPatchDto.getActorId())) {
            character.setActor(translationService.getReference(Actor.class, characterPatchDto.getActorId()));
        }
        if (nonNull(characterPatchDto.getMovieId())) {
            character.setMovie(translationService.getReference(Movie.class, characterPatchDto.getMovieId()));
        }
        Character patchedCharacter = characterRepository.save(character);

        return translationService.toReadDto(patchedCharacter);
    }

    public void deleteCharacter(UUID id) {
        characterRepository.delete(this.getCharacterRequired(id));
    }

    private Character getCharacterRequired(UUID id) {
        return characterRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Character.class, id));
    }
}
