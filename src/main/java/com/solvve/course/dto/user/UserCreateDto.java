package com.solvve.course.dto.user;

import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.dto.rating.CharacterRatingPairReadDto;
import com.solvve.course.dto.rating.MovieRatingPairReadDto;
import com.solvve.course.dto.review.CharacterReviewReadDto;
import com.solvve.course.dto.review.MovieReviewReadDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserCreateDto {

    private PrincipalReadDto principal;

    private boolean blockedReview;

    private int trustLevel;

    private List<MovieRatingPairReadDto> ratedMovies = new ArrayList<>();

    private List<CharacterRatingPairReadDto> ratedCharacter = new ArrayList<>();

    private List<PublicationReadDto> likedPosts = new ArrayList<>();

    private List<MovieReviewReadDto> likedMovieReviews = new ArrayList<>();

    private List<CharacterReviewReadDto> likedCharacterReviews = new ArrayList<>();
}
