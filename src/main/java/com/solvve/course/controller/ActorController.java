package com.solvve.course.controller;

import com.solvve.course.dto.actor.ActorCreateDto;
import com.solvve.course.dto.actor.ActorExtendedReadDto;
import com.solvve.course.dto.actor.ActorPatchDto;
import com.solvve.course.dto.actor.ActorPutDto;
import com.solvve.course.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @GetMapping("/{id}")
    public ActorExtendedReadDto getActor(@PathVariable UUID id) {
        return actorService.getActor(id);
    }

    @PostMapping
    public ActorExtendedReadDto addActor(@RequestBody ActorCreateDto actorCreateDto) {
        return actorService.addActor(actorCreateDto);
    }

    @PutMapping("/{id}")
    public ActorExtendedReadDto updateActor(@PathVariable UUID id, @RequestBody ActorPutDto actorPutDto) {
        return actorService.putActor(id, actorPutDto);
    }

    @PatchMapping("/{id}")
    public ActorExtendedReadDto patchActor(@PathVariable UUID id, @RequestBody ActorPatchDto actorPatchDto) {
        return actorService.patchActor(id, actorPatchDto);
    }

    @DeleteMapping("/{id}")
    public void deleteActor(@PathVariable UUID id) {
        actorService.deleteActor(id);
    }
}