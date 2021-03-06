package com.solvve.course.service;

import com.solvve.course.domain.Correction;
import com.solvve.course.domain.Publication;
import com.solvve.course.domain.constant.CorrectionStatus;
import com.solvve.course.dto.correction.CorrectionCreateDto;
import com.solvve.course.dto.correction.CorrectionPatchDto;
import com.solvve.course.dto.correction.CorrectionReadDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.event.AcceptCorrectionEvent;
import com.solvve.course.exception.BadCorrectionStatusException;
import com.solvve.course.repository.CorrectionRepository;
import com.solvve.course.repository.PublicationRepository;
import com.solvve.course.repository.RepositoryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PublicationCorrectionService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private CorrectionRepository correctionRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public List<CorrectionReadDto> getPublicationCorrections(UUID publicationId) {
        repositoryHelper.validateExist(Publication.class, publicationId);

        return translationService
                .translateList(correctionRepository.getAllByPublicationId(publicationId), CorrectionReadDto.class);
    }

    @Transactional
    public CorrectionReadDto addPublicationCorrection(UUID publicationId, CorrectionCreateDto correctionCreateDto) {
        Publication publication = repositoryHelper.getReferenceIfExist(Publication.class, publicationId);
        Correction correction = translationService.translate(correctionCreateDto, Correction.class);
        correction.setPublication(publication);
        correction.setStatus(CorrectionStatus.NEW);

        return translationService.translate(correctionRepository.save(correction), CorrectionReadDto.class);
    }

    public void deletePublicationCorrection(UUID id) {
        correctionRepository.delete(repositoryHelper.getEntityRequired(Correction.class, id));
    }

    @Transactional
    public PublicationReadDto acceptPublicationCorrection(UUID correctionId, UUID publicationId,
                                                          CorrectionPatchDto correctionPatchDto) {
        Correction correction = repositoryHelper.getEntityRequired(Correction.class, correctionId);
        if (!correction.getStatus().equals(CorrectionStatus.NEW)) {
            throw new BadCorrectionStatusException(correctionId, correction.getStatus());
        }

        if (correctionPatchDto.getProposedText() != null) {
            correction.setProposedText(correctionPatchDto.getProposedText());
            correction.setStatus(CorrectionStatus.ACCEPTED_AFTER_FIX);
        } else {
            correction.setStatus(CorrectionStatus.ACCEPTED);
        }

        correction = correctionRepository.save(correction);
        applicationEventPublisher.publishEvent(new AcceptCorrectionEvent(correction.getId()));

        Publication publication = repositoryHelper.getEntityRequired(Publication.class, publicationId);
        publication.setContent(this.getContentWithAcceptedCorrection(
                publication.getContent(),
                correction.getStartIndex(),
                correction.getSelectedText(),
                correction.getProposedText()
        ));

        return translationService.translate(publicationRepository.save(publication), PublicationReadDto.class);
    }

    public void changeSimilarCorrections(UUID correctionId) {
        Correction correction = repositoryHelper.getEntityRequired(Correction.class, correctionId);

        List<Correction> similarCorrections =
                correctionRepository.getSimilarCorrections(
                        correctionId,
                        correction.getPublication(),
                        correction.getSelectedText(),
                        correction.getStartIndex()
                );

        similarCorrections.forEach(similarCorrection -> {
            try {
                similarCorrection.setStatus(correction.getStatus());
                correctionRepository.save(similarCorrection);
            } catch (Exception e) {
                log.error("Failed to automatically update correction : {}", correctionId, e);
            }
        });
    }

    private String getContentWithAcceptedCorrection(String oldContent, int startIndex,
                                                    String selectedText, String proposedText) {
        StringBuilder sb = new StringBuilder();
        final char[] buff = oldContent.toCharArray();
        sb.append(buff, 0, startIndex)
                .append(proposedText)
                .append(buff, startIndex + selectedText.length(), buff.length - selectedText.length());
        return sb.toString();
    }
}