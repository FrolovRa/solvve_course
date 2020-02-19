package com.solvve.course.service;

import com.solvve.course.domain.Person;
import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.PersonRepository;
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

    public PersonReadDto getPerson(UUID id) {
        Person personFromDb = getPersonRequired(id);

        return translationService.toReadDto(personFromDb);
    }

    public PersonReadDto addPerson(PersonCreateDto personCreateDto) {
        Person person = translationService.toEntity(personCreateDto);
        person = personRepository.save(person);

        return translationService.toReadDto(person);
    }

    public PersonReadDto patchPerson(UUID id, PersonPatchDto personPatchDto) {
        Person person = this.getPersonRequired(id);
        if (nonNull(personPatchDto.getName())) {
            person.setName(personPatchDto.getName());
        }
        Person patchedPerson = personRepository.save(person);

        return translationService.toReadDto(patchedPerson);
    }

    public void deletePerson(UUID id) {
        personRepository.delete(getPersonRequired(id));
    }

    private Person getPersonRequired(UUID id) {
        return personRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Person.class, id));
    }
}