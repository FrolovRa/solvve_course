package com.solvve.course.controller;

import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationPatchDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @GetMapping("/{id}")
    public PublicationReadDto getPublication(@PathVariable UUID id) {
        return publicationService.getPublication(id);
    }

    @PostMapping
    public PublicationReadDto addPublication(@RequestBody PublicationCreateDto publicationCreateDto) {
        return publicationService.addPublication(publicationCreateDto);
    }

    @PatchMapping("/{id}")
    public PublicationReadDto patchPublication(@PathVariable UUID id,
                                               @RequestBody PublicationPatchDto publicationPatchDto) {
        return publicationService.patchPublication(id, publicationPatchDto);
    }

    @DeleteMapping("/{id}")
    public void deletePublication(@PathVariable UUID id) {
        publicationService.deletePublication(id);
    }
}