package com.solvve.course.dto.review;

import com.solvve.course.domain.constant.ReviewStatus;
import com.solvve.course.dto.complaint.movie.MovieReviewComplaintReadDto;
import com.solvve.course.dto.movie.MovieReadDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MovieReviewCreateDto {

    private UserReadDto user;

    private PrincipalReadDto moderator;

    private String content;

    private ReviewStatus status;

    private List<MovieReviewComplaintReadDto> movieReviewComplaints = new ArrayList<>();

    private List<UserReadDto> liked = new ArrayList<>();

    private MovieReadDto movie;
}
