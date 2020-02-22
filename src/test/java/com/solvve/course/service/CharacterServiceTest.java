package com.solvve.course.service;

import com.solvve.course.domain.Character;
import com.solvve.course.dto.character.CharacterCreateDto;
import com.solvve.course.dto.character.CharacterPatchDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.CharacterRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from character"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CharacterServiceTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private TestUtils utils;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void testGetCharacter() {
        Character person = utils.getCharacterFromDb();
        CharacterReadDto actualPerson = translationService.toReadDto(person);

        CharacterReadDto personReadDto = characterService.getCharacter(person.getId());

        assertThat(actualPerson).isEqualToComparingFieldByField(personReadDto);
    }

    @Test
    public void testAddCharacter() {
        CharacterCreateDto createDto = utils.createCharacterCreateDto();
        inTransaction(() -> {
            CharacterReadDto readDto = characterService.addCharacter(createDto);

            assertThat(createDto).isEqualToIgnoringGivenFields(readDto, "actorId", "movieId");
            assertNotNull(readDto.getId());

            CharacterReadDto personFromDb = characterService.getCharacter(readDto.getId());
            assertThat(readDto).isEqualToComparingFieldByField(personFromDb);
        });
    }

    @Test
    public void testPatchCharacter() {
        CharacterPatchDto characterPatchDto = new CharacterPatchDto();
        characterPatchDto.setName("Name");
        characterPatchDto.setActorId(utils.getActorFromDb().getId());
        characterPatchDto.setMovieId(utils.getMovieFromDb().getId());

        Character person = utils.getCharacterFromDb();
        CharacterReadDto patchedUser = characterService.patchPerson(person.getId(), characterPatchDto);

        assertThat(characterPatchDto)
                .isEqualToIgnoringGivenFields(patchedUser, "movieId", "actorId");
    }

    @Test
    public void testEmptyPatchCharacter() {
        CharacterPatchDto userPatchDto = new CharacterPatchDto();

        Character person = utils.getCharacterFromDb();
        CharacterReadDto patchedUser = characterService.patchPerson(person.getId(), userPatchDto);

        assertThat(translationService.toReadDto(person)).isEqualToComparingFieldByField(patchedUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonByWrongId() {
        characterService.getCharacter(UUID.randomUUID());
    }

    @Test
    public void testDeleteCharacter() {
        Character character = utils.getCharacterFromDb();

        characterService.deleteCharacter(character.getId());

        assertFalse(characterRepository.existsById(character.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        characterService.deleteCharacter(UUID.randomUUID());
    }

    @Test
    public void testCreatedAtIsSet() {
        Character character = new Character();
        character.setName("Jack Sparrow");
        character.setActor(utils.getActorFromDb());
        character.setMovie(utils.getMovieFromDb());

        character = characterRepository.save(character);

        Instant createdAtBeforeReload = character.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        character = characterRepository.findById(character.getId()).get();

        Instant createdAtAfterReload = character.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Character character = new Character();
        character.setName("Jack Sparrow");
        character.setActor(utils.getActorFromDb());
        character.setMovie(utils.getMovieFromDb());

        character = characterRepository.save(character);

        Instant updatedAtBeforeReload = character.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        character = characterRepository.findById(character.getId()).get();

        Instant updatedAtAfterReload = character.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        character.setName("Lilit");
        character = characterRepository.save(character);
        Instant updatedAtAfterUpdate = character.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }

    private void inTransaction(Runnable runnable) {
        transactionTemplate.executeWithoutResult(status -> {
            runnable.run();
        });
    }
}