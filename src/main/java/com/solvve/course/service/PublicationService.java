package com.solvve.course.service;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.Publication;
import com.solvve.course.dto.publication.PublicationCreateDto;
import com.solvve.course.dto.publication.PublicationPatchDto;
import com.solvve.course.dto.publication.PublicationReadDto;
import com.solvve.course.repository.PublicationRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PublicationService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public PublicationReadDto getPublication(UUID id) {
        Publication publicationFromDb = repositoryHelper.getEntityRequired(Publication.class, id);

        return translationService.toReadDto(publicationFromDb);
    }

    public PublicationReadDto addPublication(PublicationCreateDto userCreateDto) {
        Publication publication = translationService.toEntity(userCreateDto);
        publication = publicationRepository.save(publication);

        return translationService.toReadDto(publication);
    }

    public PublicationReadDto patchPublication(UUID id, PublicationPatchDto publicationPatchDto) {
        Publication publication = repositoryHelper.getEntityRequired(Publication.class, id);
        if (publicationPatchDto.getContent() != null) {
            publication.setContent(publicationPatchDto.getContent());
        }
        if (publicationPatchDto.getManagerId() != null) {
            publication.setManager(repositoryHelper.getReferenceIfExist(Principal.class, publicationPatchDto.getManagerId()));
        }
        if (publicationPatchDto.getTitle() != null) {
            publication.setTitle(publicationPatchDto.getTitle());
        }
        Publication patchedPublication = publicationRepository.save(publication);

        return translationService.toReadDto(patchedPublication);
    }

    public void deletePublication(UUID id) {
        publicationRepository.delete(repositoryHelper.getEntityRequired(Publication.class, id));
    }
}
