package com.solvve.course.service;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.constant.Role;
import com.solvve.course.dto.principal.PrincipalCreateDto;
import com.solvve.course.dto.principal.PrincipalPatchDto;
import com.solvve.course.dto.principal.PrincipalReadDto;
import com.solvve.course.repository.PrincipalRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<PrincipalReadDto> getPrincipalsByRole(Role role) {
        return principalRepository.getAllByRole(role)
                .stream()
                .map(translationService::toReadDto)
                .collect(Collectors.toList());
    }

    public PrincipalReadDto addPrincipal(PrincipalCreateDto principalCreateDto) {
        Principal principal = translationService.toEntity(principalCreateDto);
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
