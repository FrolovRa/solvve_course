package com.solvve.course.dto.publication;

import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.person.PersonReadDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublicationPatchDto {

    private PrincipalReadDto manager;

    private String title;

    private String content;

    private List<MovieReadDto> movies = new ArrayList<>();

    private List<PersonReadDto> persons = new ArrayList<>();

    private List<UserReadDto> liked = new ArrayList<>();
}
