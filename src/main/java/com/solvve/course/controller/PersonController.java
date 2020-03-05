package com.solvve.course.controller;

import com.solvve.course.dto.person.PersonCreateDto;
import com.solvve.course.dto.person.PersonPatchDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/{id}")
    public PersonReadDto getPerson(@PathVariable UUID id) {
        return personService.getPerson(id);
    }

    @PostMapping
    public PersonReadDto addPerson(@RequestBody PersonCreateDto personCreateDto) {
        return personService.addPerson(personCreateDto);
    }

    @PatchMapping("/{id}")
    public PersonReadDto patchPerson(@PathVariable UUID id, @RequestBody PersonPatchDto personPatchDto) {
        return personService.patchPerson(id, personPatchDto);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable UUID id) {
        personService.deletePerson(id);
    }
}