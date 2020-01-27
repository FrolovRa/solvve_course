package com.solvve.course.dto.person;

import com.solvve.course.dto.publication.PublicationReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonPatchDto {

    private String name;

    private List<PublicationReadDto> posts = new ArrayList<>();
}
