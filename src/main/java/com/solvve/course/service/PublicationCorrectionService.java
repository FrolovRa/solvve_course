package com.solvve.course.service;

import com.solvve.course.domain.Correction;
import com.solvve.course.domain.Publication;
import com.solvve.course.domain.constant.CorrectionStatus;
import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.repository.CorrectionRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublicationCorrectionService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public List<CorrectionReadDto> getPublicationCorrections(UUID publicationId) {
        repositoryHelper.validateExist(Publication.class, publicationId);

        return correctionRepository.getAllByPublicationId(publicationId).stream()
                .map(translationService::toReadDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CorrectionReadDto addPublicationCorrection(UUID publicationId, CorrectionCreateDto userCreateDto) {
        Publication publication = repositoryHelper.getReferenceIfExist(Publication.class, publicationId);
        Correction correction = translationService.toEntity(userCreateDto);
        correction.setPublication(publication);
        correction.setStatus(CorrectionStatus.NEW);

        return translationService.toReadDto(correctionRepository.save(correction));
    }

    public void deletePublicationCorrection(UUID id) {
        correctionRepository.delete(repositoryHelper.getEntityRequired(Correction.class, id));
    }
}