package com.solvve.course.service;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.User;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.repository.RepositoryHelper;
import com.solvve.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public UserReadDto getUser(UUID id) {
        User userFromDb = repositoryHelper.getEntityRequired(User.class, id);

        return translationService.translate(userFromDb, UserReadDto.class);
    }

    @Transactional
    public UserReadDto addUser(UserCreateDto userCreateDto) {
        User user = translationService.toEntity(userCreateDto);
        user = userRepository.save(user);

        return translationService.translate(user, UserReadDto.class);
    }

    public UserReadDto patchUser(UUID id, UserPatchDto userPatchDto) {
        User user = repositoryHelper.getEntityRequired(User.class, id);
        if (nonNull(userPatchDto.getPrincipalId())) {
            user.setPrincipal(repositoryHelper.getReferenceIfExist(Principal.class, userPatchDto.getPrincipalId()));
        }
        if (nonNull(userPatchDto.getTrustLevel())) {
            user.setTrustLevel(userPatchDto.getTrustLevel());
        }
        if (nonNull(userPatchDto.getBlockedReview())) {
            user.setBlockedReview(userPatchDto.getBlockedReview());
        }
        User patchedActor = userRepository.save(user);

        return translationService.translate(patchedActor, UserReadDto.class);
    }

    public void deleteUser(UUID id) {
        userRepository.delete(repositoryHelper.getEntityRequired(User.class, id));
    }
}
