package com.solvve.course.service;

import com.solvve.course.domain.Correction;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.repository.CorrectionRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CorrectionService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public CorrectionReadDto getCorrection(UUID id) {
        Correction correctionFromDb = repositoryHelper.getEntityRequired(Correction.class, id);

        return translationService.translate(correctionFromDb, CorrectionReadDto.class);
    }

    public List<CorrectionReadDto> getAllCorrections() {
        return translationService.translateList(correctionRepository.getAll(), CorrectionReadDto.class);
    }

    public void deleteCorrection(UUID id) {
        correctionRepository.delete(repositoryHelper.getEntityRequired(Correction.class, id));
    }
}
