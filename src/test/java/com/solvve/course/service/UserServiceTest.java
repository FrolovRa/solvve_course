package com.solvve.course.service;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.User;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class UserServiceTest extends BaseTest {

    @Test
    public void testGetUser() {
        User user = utils.getUserFromDb();
        UserReadDto actualUser = translationService.translate(user, UserReadDto.class);

        UserReadDto userReadDto = userService.getUser(user.getId());

        assertThat(actualUser).isEqualToComparingFieldByField(userReadDto);
    }

    @Test
    public void testAddUser() {
        UserCreateDto createDto = utils.createUserCreateDto();

        UserReadDto readDto = userService.addUser(createDto);

        assertThat(createDto).isEqualToIgnoringGivenFields(readDto, "principalId");
        assertNotNull(readDto.getId());

        UserReadDto userFromDb = userService.getUser(readDto.getId());
        assertThat(readDto).isEqualToComparingFieldByField(userFromDb);
    }

    @Test
    public void testPatchUser() {
        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setPrincipalId(utils.getPrincipalFromDb().getId());
        userPatchDto.setBlockedReview(true);
        userPatchDto.setTrustLevel(2);

        User user = utils.getUserFromDb();
        UserReadDto patchedUser = userService.patchUser(user.getId(), userPatchDto);

        assertThat(userPatchDto).isEqualToIgnoringGivenFields(patchedUser, "principalId");
    }

    @Test
    public void testEmptyPatchUser() {
        UserPatchDto userPatchDto = new UserPatchDto();

        User user = utils.getUserFromDb();
        UserReadDto patchedUser = userService.patchUser(user.getId(), userPatchDto);

        assertThat(patchedUser).isEqualToIgnoringGivenFields(user, "principal");
        assertThat(patchedUser.getPrincipal()).isEqualToComparingFieldByField(patchedUser.getPrincipal());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserByWrongId() {
        userService.getUser(UUID.randomUUID());
    }

    @Test
    public void testDeleteUser() {
        User user = utils.getUserFromDb();

        userService.deleteUser(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        userService.deleteUser(UUID.randomUUID());
    }

    @Test
    public void testCreatedAtIsSet() {
        User user = new User();
        user.setPrincipal(utils.getPrincipalFromDb());

        user = userRepository.save(user);

        Instant createdAtBeforeReload = user.getCreatedAt();
        assertNotNull(createdAtBeforeReload);
        user = userRepository.findById(user.getId()).get();

        Instant createdAtAfterReload = user.getCreatedAt();
        assertNotNull(createdAtAfterReload);
        assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testUpdatedAtIsSet() {
        User user = new User();
        user.setPrincipal(utils.getPrincipalFromDb());

        user = userRepository.save(user);

        Instant updatedAtBeforeReload = user.getCreatedAt();
        assertNotNull(updatedAtBeforeReload);
        user = userRepository.findById(user.getId()).get();

        Instant updatedAtAfterReload = user.getCreatedAt();
        assertNotNull(updatedAtAfterReload);
        assertEquals(updatedAtBeforeReload, updatedAtAfterReload);

        user.setTrustLevel(2);
        user = userRepository.save(user);
        Instant updatedAtAfterUpdate = user.getUpdatedAt();

        assertNotEquals(updatedAtAfterUpdate, updatedAtAfterReload);
    }
}
