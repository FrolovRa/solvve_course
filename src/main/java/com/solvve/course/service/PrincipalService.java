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

        return translationService.translate(principalFromDb, PrincipalReadDto.class);
    }

    public List<PrincipalReadDto> getPrincipalsByRole(Role role) {
        return principalRepository.getAllByRoles(role)
                .stream()
                .map(principal -> translationService.translate(principal, PrincipalReadDto.class))
                .collect(Collectors.toList());
    }

    public PrincipalReadDto addPrincipal(PrincipalCreateDto principalCreateDto) {
        Principal principal = translationService.translate(principalCreateDto, Principal.class);
        principal = principalRepository.save(principal);

        return translationService.translate(principal, PrincipalReadDto.class);
    }

    public PrincipalReadDto patchPrincipal(UUID id, PrincipalPatchDto principalPatchDto) {
        Principal principal = repositoryHelper.getEntityRequired(Principal.class, id);

        translationService.map(principalPatchDto, principal);
        Principal patchedPrincipal = principalRepository.save(principal);

        return translationService.translate(patchedPrincipal, PrincipalReadDto.class);
    }

    public void deletePrincipal(UUID id) {
        principalRepository.delete(repositoryHelper.getEntityRequired(Principal.class, id));
    }
}
