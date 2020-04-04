package com.solvve.course.dto.person;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PersonCreateDto {

    @NotNull
    private String name;
}
