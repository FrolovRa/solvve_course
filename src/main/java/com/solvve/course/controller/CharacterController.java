package com.solvve.course.controller;

import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterPatchDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @GetMapping("/{id}")
    public CharacterReadDto getCharacter(@PathVariable UUID id) {
        return characterService.getCharacter(id);
    }

    @PostMapping
    public CharacterReadDto addCharacter(@RequestBody CharacterCreateDto characterCreateDto) {
        return characterService.addCharacter(characterCreateDto);
    }

    @PatchMapping("/{id}")
    public CharacterReadDto patchCharacter(@PathVariable UUID id, @RequestBody CharacterPatchDto characterPatchDto) {
        return characterService.patchCharacter(id, characterPatchDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCharacter(@PathVariable UUID id) {
        characterService.deleteCharacter(id);
    }
}