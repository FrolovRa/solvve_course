package com.solvve.course.controller;

import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.service.CorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/corrections")
public class CorrectionController {

    @Autowired
    private CorrectionService correctionService;

    @GetMapping
    public List<CorrectionReadDto> getAllCorrections() {
        return correctionService.getAllCorrections();
    }

    @GetMapping("/{id}")
    public CorrectionReadDto getCorrection(@PathVariable UUID id) {
        return correctionService.getCorrection(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCorrection(@PathVariable UUID id) {
        correctionService.deleteCorrection(id);
    }
}