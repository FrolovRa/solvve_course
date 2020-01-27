package com.solvve.course.dto.person;

import com.solvve.course.dto.publication.PublicationReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PersonReadDto {

    private UUID id;

    private String name;

    private List<PublicationReadDto> posts = new ArrayList<>();
}
