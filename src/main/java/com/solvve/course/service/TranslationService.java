package com.solvve.course.service;

import com.solvve.course.domain.Character;
import com.solvve.course.domain.User;
import com.solvve.course.dto.character.CharacterReadDto;
import com.solvve.course.dto.user.UserReadDto;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
public class TranslationService {
    public UserReadDto toReadDto(User user) {
        UserReadDto dto = new UserReadDto();
        dto.setId(user.getId());
        dto.setPrincipal(this.toReadDto(user.getPrincipal()));
        dto.setBlockedReview(user.isBlockedReview());
        dto.setTrustLevel(user.getTrustLevel());
        dto.setRatedCharacter(user.getRatedCharacter()
                .stream()
                .map(this::toReadDto)
                .collect(toList()));
        dto.setLikedPosts(user.getLikedPosts()
                .stream()
                .map(this::toReadDto)
                .collect(toList()));
        dto.setRatedMovies(user.getRatedMovies()
                .stream()
                .map(this::toReadDto)
                .collect(toList()));
        dto.setLikedCharacterReviews(user.getLikedCharacterReviews()
                .stream()
                .map(this::toReadDto)
                .collect(toList()));
        dto.setLikedMovieReviews(user.getLikedMovieReviews()
                .stream()
                .map(this::toReadDto)
                .collect(toList()));
    }

    public CharacterReadDto toReadDto(Character character) {
        CharacterReadDto dto = new CharacterReadDto();
        dto.setId(character.getId());
        dto.setActor(this.toReadDto(character.getActor()));
        dto.setMovie(this.toReadDto(character.getMovie()));
        dto.setName(character.getName());
        dto.setReviews(character.getReviews()
                .stream()
                .map(this::toReadDto)
                .collect(toList()));
    }
}