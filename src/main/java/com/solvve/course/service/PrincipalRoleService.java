package com.solvve.course.service;

import com.solvve.course.domain.Principal;
import com.solvve.course.domain.PrincipalRole;
import com.solvve.course.dto.role.PrincipalRoleReadDto;
import com.solvve.course.exception.LinkDuplicatedException;
import com.solvve.course.repository.PrincipalRepository;
import com.solvve.course.repository.PrincipalRoleRepository;
import com.solvve.course.repository.RepositoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PrincipalRoleService {

    @Autowired
    private RepositoryHelper repositoryHelper;

    @Autowired
    private PrincipalRoleRepository principalRoleRepository;

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public List<PrincipalRoleReadDto> getAllRoles() {
        return translationService
                .translateList(principalRoleRepository.getAll(), PrincipalRoleReadDto.class);
    }

    @Transactional
    public List<PrincipalRoleReadDto> addRoleToPrincipal(UUID principalId, UUID roleId) {
        Principal principal = repositoryHelper.getEntityRequired(Principal.class, principalId);
        PrincipalRole principalRole = repositoryHelper.getEntityRequired(PrincipalRole.class, roleId);

        if (principal.getRoles().contains(principalRole)) {
            throw new LinkDuplicatedException(String.format(
                    "Principal %s already has role %s",
                    principalId,
                    principalRole.getRole())
            );
        }

        principal.getRoles().add(principalRole);
        principal = principalRepository.save(principal);

        return translationService.translateList(principal.getRoles(), PrincipalRoleReadDto.class);
    }
}
