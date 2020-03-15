package com.solvve.course.controller;

import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.correction.CorrectionPatchDto;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.service.PublicationCorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/publications/{publicationId}/corrections")
public class PublicationCorrectionController {

    @Autowired
    private PublicationCorrectionService publicationCorrectionService;

    @GetMapping
    public List<CorrectionReadDto> getPublicationCorrections(@PathVariable UUID publicationId) {
        return publicationCorrectionService.getPublicationCorrections(publicationId);
    }

    @PostMapping
    public CorrectionReadDto addPublicationCorrection(@PathVariable UUID publicationId,
                                                      @RequestBody CorrectionCreateDto correctionCreateDto) {
        return publicationCorrectionService.addPublicationCorrection(publicationId, correctionCreateDto);
    }

    @PatchMapping("/{correctionId}/accept")
    public PublicationReadDto acceptPublicationCorrection(@PathVariable UUID publicationId,
                                                          @PathVariable UUID correctionId,
                                                          @RequestBody CorrectionPatchDto correctionPatchDto) {
        return publicationCorrectionService
                .acceptPublicationCorrection(correctionId, publicationId, correctionPatchDto);
    }

    @DeleteMapping("/{correctionId}")
    public void deletePublicationCorrection(@PathVariable UUID correctionId) {
        publicationCorrectionService.deletePublicationCorrection(correctionId);
    }
}