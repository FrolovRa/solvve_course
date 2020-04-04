package com.solvve.course.controller;

import com.solvve.course.dto.user.UserCreateDto;
import com.solvve.course.dto.user.UserPatchDto;
import com.solvve.course.dto.user.UserReadDto;
import com.solvve.course.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserReadDto getPrincipal(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PostMapping
    public UserReadDto addPrincipal(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userService.addUser(userCreateDto);
    }

    @PatchMapping("/{id}")
    public UserReadDto patchPrincipal(@PathVariable UUID id, @RequestBody UserPatchDto userPatchDto) {
        return userService.patchUser(id, userPatchDto);
    }

    @DeleteMapping("/{id}")
    public void deletePrincipal(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}