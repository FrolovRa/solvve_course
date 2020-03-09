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

import static java.util.Objects.nonNull;

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

        return translationService.toReadDto(personFromDb);
    }

    public PersonReadDto addPerson(PersonCreateDto personCreateDto) {
        Person person = translationService.toEntity(personCreateDto);
        person = personRepository.save(person);

        return translationService.toReadDto(person);
    }

    public PersonReadDto patchPerson(UUID id, PersonPatchDto personPatchDto) {
        Person person = repositoryHelper.getEntityRequired(Person.class, id);
        if (nonNull(personPatchDto.getName())) {
            person.setName(personPatchDto.getName());
        }
        Person patchedPerson = personRepository.save(person);

        return translationService.toReadDto(patchedPerson);
    }

    public void deletePerson(UUID id) {
        personRepository.delete(repositoryHelper.getEntityRequired(Person.class, id));
    }
}