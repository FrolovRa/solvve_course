package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Person;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
import com.solvve.course.dto.actor.ActorPutDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.ActorRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from actor",
        "delete from movie",
        "delete from person"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ActorServiceTest {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private PersonService personService;

    @Test
    @Transactional
    public void testGetActor() {
        Actor actor = utils.getActorFromDb();
        ActorExtendedReadDto actualActor = translationService.toExtendedReadDto(actor);

        ActorExtendedReadDto actorExtendedReadDto = actorService.getActor(actor.getId());

        assertThat(actualActor).isEqualToComparingFieldByField(actorExtendedReadDto);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetActorByWrongId() {
        actorService.getActor(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testAddActor() {
        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPersonId(utils.getPersonFromDb().getId());

        ActorExtendedReadDto actorExtendedReadDto = actorService.addActor(actorCreateDto);

        assertThat(actorCreateDto).isEqualToIgnoringGivenFields(actorExtendedReadDto, "personId");
        assertNotNull(actorExtendedReadDto.getId());

        ActorExtendedReadDto actorFromDb = actorService.getActor(actorExtendedReadDto.getId());
        assertThat(actorExtendedReadDto).isEqualToComparingFieldByField(actorFromDb);
    }

    @Test
    @Transactional
    public void testPatchActor() {
        ActorPatchDto actorPatchDto = new ActorPatchDto();
        Person person = utils.getPersonFromDb();
        actorPatchDto.setPersonId(person.getId());

        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPersonId(person.getId());

        ActorExtendedReadDto actorExtendedReadDto = actorService.addActor(actorCreateDto);
        ActorExtendedReadDto patchedActor = actorService.patchActor(actorExtendedReadDto.getId(), actorPatchDto);

        assertEquals(actorExtendedReadDto.getPerson(), patchedActor.getPerson());
    }

    @Test
    @Transactional
    public void testEmptyPatchActor() {
        ActorPatchDto actorPatchDto = new ActorPatchDto();

        PersonReadDto personReadDto = personService.addPerson(utils.createPersonCreateDto());
        ActorCreateDto actorCreateDto = utils.createActorCreateDto();
        actorCreateDto.setPersonId(personReadDto.getId());

        ActorExtendedReadDto actorExtendedReadDto = actorService.addActor(actorCreateDto);
        ActorExtendedReadDto patchedActor = actorService.patchActor(actorExtendedReadDto.getId(), actorPatchDto);

        assertThat(actorExtendedReadDto).isEqualToIgnoringGivenFields(patchedActor);
    }

    @Test
    @Transactional
    public void testPutActor() {
        ActorPutDto actorPutDto = new ActorPutDto();
        PersonReadDto personReadDtoForPut = personService.addPerson(utils.createPersonCreateDto());
        actorPutDto.setPersonId(personReadDtoForPut.getId());

        PersonReadDto personReadDtoForCreate = personService.addPerson(utils.createPersonCreateDto());
        ActorCreateDto actorCreateDto = utils.createActorCreateDto();
        actorCreateDto.setPersonId(personReadDtoForCreate.getId());
        ActorExtendedReadDto actorExtendedReadDto = actorService.addActor(actorCreateDto);
        ActorExtendedReadDto updatedActor = actorService.updateActor(actorExtendedReadDto.getId(), actorPutDto);

        assertThat(actorPutDto).isEqualToIgnoringGivenFields(updatedActor, "personId");
    }

    @Test
    @Transactional
    public void testDeleteActor() {
        Actor actor = utils.getActorFromDb();

        actorService.deleteActor(actor.getId());

        assertFalse(actorRepository.existsById(actor.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        actorService.deleteActor(UUID.randomUUID());
    }

}
