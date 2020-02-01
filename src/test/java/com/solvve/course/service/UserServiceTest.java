package com.solvve.course.service;

import com.solvve.course.domain.User;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.exception.EntityNotFoundException;
import com.solvve.course.repository.UserRepository;
import com.solvve.course.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {
        "delete from user",
        "delete from principal"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private TranslationService translationService;
    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void testGetUser() {
        User user = utils.getUserFromDb();
        UserReadDto actualUser = translationService.toReadDto(user);

        UserReadDto userReadDto = userService.getUser(user.getId());

        assertThat(actualUser).isEqualToComparingFieldByField(userReadDto);
    }

    @Test
    public void testAddUser() {
        UserCreateDto createDto = utils.createUserCreateDto();

        UserReadDto readDto = userService.addUser(createDto);

        assertThat(createDto).isEqualToComparingFieldByField(readDto);
        assertNotNull(readDto.getId());

        UserReadDto userFromDb = userService.getUser(readDto.getId());
        assertThat(readDto).isEqualToComparingFieldByField(userFromDb);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetUserByWrongId() {
        userService.getUser(UUID.randomUUID());
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        User user = utils.getUserFromDb();

        userService.deleteUser(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByWrongId() {
        userService.deleteUser(UUID.randomUUID());
    }
}
