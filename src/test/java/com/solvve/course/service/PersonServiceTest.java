package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Person;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class PersonServiceTest extends BaseTest {

    @Test
    public void testGetPerson() {
        Person person = utils.getPersonFromDb();
        PersonReadDto actualPerson = translationService.translate(person, PersonReadDto.class);

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
        PersonPatchDto personPatchDto = new PersonPatchDto();
        personPatchDto.setName("Name");

        Person person = utils.getPersonFromDb();
        PersonReadDto patchedPerson = personService.patchPerson(person.getId(), personPatchDto);

        assertThat(patchedPerson).isEqualToIgnoringGivenFields(person, "name", "updatedAt");
        assertEquals(patchedPerson.getName(), personPatchDto.getName());
    }

    @Test
    public void testEmptyPatchPerson() {
        PersonPatchDto userPatchDto = new PersonPatchDto();

        Person person = utils.getPersonFromDb();
        PersonReadDto patchedUser = personService.patchPerson(person.getId(), userPatchDto);

        assertThat(person).isEqualToComparingFieldByField(patchedUser);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetPersonByWrongId() {
        personService.getPerson(UUID.randomUUID());
    }

    @Test
    public void testDeletePerson() {
        Person person = utils.getPersonFromDb();

        personService.deletePerson(person.getId());

        assertFalse(personRepository.existsById(person.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        personService.deletePerson(UUID.randomUUID());
    }

    @Test
    public void testCreatedAtIsSet() {
        Person person = new Person();
        person.setName("Udjin");
        person.setBirthDate(LocalDate.of(2000,10,10));

        person = personRepository.save(person);

        Instant createdAtBeforeReload = person.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        person = personRepository.findById(person.getId()).get();

        Instant createdAtAfterReload = person.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        Person person = new Person();
        person.setName("Udjin");
        person.setBirthDate(LocalDate.of(2000,10,10));

        person = personRepository.save(person);

        Instant updatedAtBeforeReload = person.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        person = personRepository.findById(person.getId()).get();

        Instant updatedAtAfterReload = person.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        person.setName("Manny");
        person = personRepository.save(person);
        Instant updatedAtAfterUpdate = person.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }
}