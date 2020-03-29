package com.solvve.course.service;

import com.solvve.course.domain.Character;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterPatchDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.repository.CharacterRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public CharacterReadDto getCharacter(UUID id) {
        Character characterFromDb = repositoryHelper.getEntityRequired(Character.class, id);

        return translationService.translate(characterFromDb, CharacterReadDto.class);
    }

    public CharacterReadDto addCharacter(CharacterCreateDto movieCreateDto) {
        Character character = translationService.translate(movieCreateDto, Character.class);
        character = characterRepository.save(character);

        return translationService.translate(character, CharacterReadDto.class);
    }

    @Transactional
    public CharacterReadDto patchCharacter(UUID id, CharacterPatchDto characterPatchDto) {
        Character character = repositoryHelper.getEntityRequired(Character.class, id);

        translationService.patchEntity(characterPatchDto, character);
        Character patchedCharacter = characterRepository.save(character);

        return translationService.translate(patchedCharacter, CharacterReadDto.class);
    }

    public void deleteCharacter(UUID id) {
        characterRepository.delete(repositoryHelper.getEntityRequired(Character.class, id));
    }
}