package com.solvve.course.service;

import com.solvve.course.domain.User;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TranslationService translationService;

    public UserReadDto getUser(UUID id) {
        User userFromDb = getUserRequired(id);
        return translationService.toReadDto(userFromDb);
    }

    public UserReadDto addUser(UserCreateDto userCreateDto) {
        User user = translationService.toEntity(userCreateDto);
        user = userRepository.save(user);
        return translationService.toReadDto(user);
    }

    public UserReadDto patchUser(UUID id, UserPatchDto userPatchDto) {
        User user = this.getUserRequired(id);

        if (nonNull(userPatchDto.getPrincipal())) {
            user.setPrincipal(translationService.toEntity(userPatchDto.getPrincipal()));
        }

        if (nonNull(userPatchDto.getTrustLevel())) {
            user.setTrustLevel(userPatchDto.getTrustLevel());
        }

        if (nonNull(userPatchDto.getBlockedReview())) {
            user.setBlockedReview(userPatchDto.getBlockedReview());
        }

        User patchedActor = userRepository.save(user);

        return translationService.toReadDto(patchedActor);
    }

    public void deleteUser(UUID id) {
        userRepository.delete(getUserRequired(id));
    }

    private User getUserRequired(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }
}
