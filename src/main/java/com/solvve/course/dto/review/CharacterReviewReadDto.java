package com.solvve.course.dto.review;

import com.solvve.course.domain.constant.ReviewStatus;
import com.solvve.course.dto.complaint.movie.MovieReviewComplaintReadDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CharacterReviewReadDto {

    private UUID id;

    private UserReadDto user;

    private PrincipalReadDto moderator;

    private String content;

    private ReviewStatus status;

    private List<MovieReviewComplaintReadDto> movieReviewComplaints = new ArrayList<>();

    private List<UserReadDto> liked = new ArrayList<>();

    private Character character;
}
