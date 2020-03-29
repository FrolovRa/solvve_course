package com.solvve.course.service;

import com.solvve.course.domain.Person;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.repository.PersonRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public PersonReadDto getPerson(UUID id) {
        Person personFromDb = repositoryHelper.getEntityRequired(Person.class, id);

        return translationService.translate(personFromDb, PersonReadDto.class);
    }

    public PersonReadDto addPerson(PersonCreateDto personCreateDto) {
        Person person = translationService.translate(personCreateDto, Person.class);
        person = personRepository.save(person);

        return translationService.translate(person, PersonReadDto.class);
    }

    public PersonReadDto patchPerson(UUID id, PersonPatchDto personPatchDto) {
        Person person = repositoryHelper.getEntityRequired(Person.class, id);

        translationService.patchEntity(personPatchDto, person);
        Person patchedPerson = personRepository.save(person);

        return translationService.translate(patchedPerson, PersonReadDto.class);
    }

    public void deletePerson(UUID id) {
        personRepository.delete(repositoryHelper.getEntityRequired(Person.class, id));
    }
}