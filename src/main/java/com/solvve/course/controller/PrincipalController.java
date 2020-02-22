package com.solvve.course.controller;

import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/principals")
public class PrincipalController {

    @Autowired
    private PrincipalService principalService;

    @GetMapping("/{id}")
    public PrincipalReadDto getPrincipal(@PathVariable UUID id) {
        return principalService.getPrincipal(id);
    }

    @PostMapping
    public PrincipalReadDto addPrincipal(@RequestBody PrincipalCreateDto principalCreateDto) {
        return principalService.addPrincipal(principalCreateDto);
    }

    @PatchMapping("/{id}")
    public PrincipalReadDto patchPrincipal(@PathVariable UUID id, @RequestBody PrincipalPatchDto principalPatchDto) {
        return principalService.patchPrincipal(id, principalPatchDto);
    }

    @DeleteMapping("/{id}")
    public void deletePrincipal(@PathVariable UUID id) {
        principalService.deletePrincipal(id);
    }
}