package com.solvve.course.controller;

import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserReadDto getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    @GetMapping("/{id}/corrections")
    public List<CorrectionReadDto> getUserCorrections(@PathVariable UUID id) {
        return userService.getUserCorrections(id);
    }

    @PostMapping
    public UserReadDto addUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.addUser(userCreateDto);
    }

    @PatchMapping("/{id}")
    public UserReadDto patchUser(@PathVariable UUID id,
                                 @RequestBody UserPatchDto userPatchDto) {
        return userService.patchUser(id, userPatchDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}