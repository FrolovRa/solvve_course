package com.solvve.course.dto.person;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PersonCreateDto {

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthDate;
}
