package com.solvve.course.dto.person;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonPatchDto {

    private String name;

    private LocalDate birthDate;
}
