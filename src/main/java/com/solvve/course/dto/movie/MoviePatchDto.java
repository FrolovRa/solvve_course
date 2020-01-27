package com.solvve.course.dto.movie;

import com.solvve.course.domain.*;
import com.solvve.course.domain.Character;
import com.solvve.course.domain.constant.Genre;
import com.solvve.course.dto.actor.ActorReadDto;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.director.DirectorReadDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.dto.review.MovieReviewReadDto;
import com.solvve.course.dto.writer.WriterReadDto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MoviePatchDto {

    private String name;

    private String description;

    private Set<Genre> genres;

    private List<CharacterReadDto> characters;

    private List<ActorReadDto> cast;

    private List<ActorReadDto> stars;

    private List<DirectorReadDto> directors;

    private List<WriterReadDto> writers;

    private List<MovieReviewReadDto> reviews;

    private List<PublicationReadDto> posts;
}
