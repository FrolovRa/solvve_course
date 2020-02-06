package com.solvve.course.service;

import com.solvve.course.domain.Person;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.PersonRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from person"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PersonServiceTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private PersonService personService;

    @Test
    @Transactional
    public void testGetPerson() {
        Person person = utils.getPersonFromDb();
        PersonReadDto actualPerson = translationService.toReadDto(person);

        PersonReadDto personReadDto = personService.getPerson(person.getId());

        assertThat(actualPerson).isEqualToComparingFieldByField(personReadDto);
    }

    @Test
    public void testAddPerson() {
        PersonCreateDto createDto = utils.createPersonCreateDto();

        PersonReadDto readDto = personService.addPerson(createDto);

        assertThat(createDto).isEqualToComparingFieldByField(readDto);
        assertNotNull(readDto.getId());

        PersonReadDto personFromDb = personService.getPerson(readDto.getId());
        assertThat(readDto).isEqualToComparingFieldByField(personFromDb);
    }

    @Test
    public void testPatchPerson() {
        PersonPatchDto userPatchDto = new PersonPatchDto();
        userPatchDto.setName("Name");

        Person person = utils.getPersonFromDb();
        PersonReadDto patchedUser = personService.patchPerson(person.getId(), userPatchDto);

        assertThat(userPatchDto).isEqualToComparingFieldByField(patchedUser);
    }

    @Test
    public void testEmptyPatchPerson() {
        PersonPatchDto userPatchDto = new PersonPatchDto();

        Person person = utils.getPersonFromDb();
        PersonReadDto patchedUser = personService.patchPerson(person.getId(), userPatchDto);

        assertThat(translationService.toReadDto(person)).isEqualToComparingFieldByField(patchedUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonByWrongId() {
        personService.getPerson(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testDeletePerson() {
        Person person = utils.getPersonFromDb();

        personService.deletePerson(person.getId());

        assertFalse(personRepository.existsById(person.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        personService.deletePerson(UUID.randomUUID());
    }
}