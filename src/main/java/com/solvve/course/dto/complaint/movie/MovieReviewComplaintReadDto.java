package com.solvve.course.dto.complaint.movie;

import com.solvve.course.domain.constant.ComplaintReason;
import com.solvve.course.dto.review.MovieReviewReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

import java.util.UUID;

@Data
public class MovieReviewComplaintReadDto {

    private UUID id;

    private UserReadDto user;

    private MovieReviewReadDto review;

    private ComplaintReason reason;

    private String comment;
}
