package com.solvve.course.service;

import com.solvve.course.domain.Principal;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.repository.PrincipalRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class PrincipalService {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private RepositoryHelper repositoryHelper;

    public PrincipalReadDto getPrincipal(UUID id) {
        Principal principalFromDb = repositoryHelper.getEntityRequired(Principal.class, id);

        return translationService.toReadDto(principalFromDb);
    }

    public PrincipalReadDto addPrincipal(PrincipalCreateDto userCreateDto) {
        Principal principal = translationService.toEntity(userCreateDto);
        principal = principalRepository.save(principal);

        return translationService.toReadDto(principal);
    }

    public PrincipalReadDto patchPrincipal(UUID id, PrincipalPatchDto principalPatchDto) {
        Principal principal = repositoryHelper.getEntityRequired(Principal.class, id);
        if (nonNull(principalPatchDto.getName())) {
            principal.setName(principalPatchDto.getName());
        }
        if (nonNull(principalPatchDto.getRole())) {
            principal.setRole(principalPatchDto.getRole());
        }
        if (nonNull(principalPatchDto.getEmail())) {
            principal.setEmail(principalPatchDto.getEmail());
        }
        if (nonNull(principalPatchDto.getBlocked())) {
            principal.setBlocked(principalPatchDto.getBlocked());
        }
        Principal patchedPrincipal = principalRepository.save(principal);

        return translationService.toReadDto(patchedPrincipal);
    }

    public void deletePrincipal(UUID id) {
        principalRepository.delete(repositoryHelper.getEntityRequired(Principal.class, id));
    }
}
