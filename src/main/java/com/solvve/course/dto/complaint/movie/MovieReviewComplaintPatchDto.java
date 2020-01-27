package com.solvve.course.dto.complaint.movie;

import com.solvve.course.domain.constant.ComplaintReason;
import com.solvve.course.dto.review.CharacterReviewReadDto;
import com.solvve.course.dto.user.UserReadDto;
import lombok.Data;

@Data
public class MovieReviewComplaintPatchDto {

    private UserReadDto user;

    private CharacterReviewReadDto review;

    private ComplaintReason reason;

    private String comment;
}
