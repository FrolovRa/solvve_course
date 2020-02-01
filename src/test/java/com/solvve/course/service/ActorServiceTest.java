package com.solvve.course.service;

import com.solvve.course.domain.Actor;
import com.solvve.course.domain.Person;
import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
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

    @Test
    @Transactional
    public void testGetActor() {
        Actor actor = utils.getActorFromDb();
        ActorExtendedReadDto actualActor = translationService.toExtendedReadDto(actor);

        ActorExtendedReadDto actorExtendedReadDto = actorService.getActor(actor.getId());

        assertThat(actualActor).isEqualToComparingFieldByField(actorExtendedReadDto);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetMovieByWrongId() {
        actorService.getActor(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testCreateActor() {
        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPerson(translationService.toReadDto(utils.getPersonFromDb()));
        actorCreateDto.setMovies(Collections.singletonList(translationService.toReadDto(utils.getMovieFromDb())));

        ActorExtendedReadDto actorExtendedReadDto = actorService.addActor(actorCreateDto);

        assertThat(actorCreateDto).isEqualToComparingFieldByField(actorExtendedReadDto);
        assertNotNull(actorExtendedReadDto.getId());

        ActorExtendedReadDto actorFromDb = actorService.getActor(actorExtendedReadDto.getId());
        assertThat(actorExtendedReadDto).isEqualToComparingFieldByField(actorFromDb);
    }

    @Test
    @Transactional
    public void testPatchActor() {
        ActorPatchDto actorPatchDto = new ActorPatchDto();
        actorPatchDto.setMovies(Collections.singletonList(translationService.toReadDto(utils.getMovieFromDb())));
        actorPatchDto.setMoviesAsStar(Collections.singletonList(translationService.toReadDto(utils.getMovieFromDb())));
        actorPatchDto.setCharacters(Collections.emptyList());

        Person person = utils.getPersonFromDb();
        actorPatchDto.setPerson(translationService.toReadDto(person));

        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPerson(translationService.toReadDto(person));

        ActorExtendedReadDto actorExtendedReadDto = actorService.addActor(actorCreateDto);
        ActorExtendedReadDto patchedActor = actorService.patchActor(actorExtendedReadDto.getId(), actorPatchDto);

        assertEquals(actorExtendedReadDto.getPerson(), patchedActor.getPerson());
        assertEquals(actorPatchDto.getMovies(), patchedActor.getMovies());
        assertEquals(actorPatchDto.getMoviesAsStar(), patchedActor.getMoviesAsStar());
        assertEquals(actorPatchDto.getCharacters(), patchedActor.getCharacters());
    }

    @Test
    @Transactional
    public void testEmptyPatchMovie() {
        ActorPatchDto actorPatchDto = new ActorPatchDto();

        ActorCreateDto actorCreateDto = new ActorCreateDto();
        actorCreateDto.setPerson(translationService.toReadDto(utils.getPersonFromDb()));
        actorCreateDto.setMovies(Collections.singletonList(translationService.toReadDto(utils.getMovieFromDb())));

        ActorExtendedReadDto actorExtendedReadDto = actorService.addActor(actorCreateDto);
        ActorExtendedReadDto patchedActor = actorService.patchActor(actorExtendedReadDto.getId(), actorPatchDto);

        assertThat(actorExtendedReadDto).isEqualToIgnoringGivenFields(patchedActor);
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
