package com.solvve.course.security;

import com.solvve.course.repository.PrincipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentPrincipalValidator {

    @Autowired
    private PrincipalRepository principalRepository;

    @Autowired
    private AuthenticationResolver authenticationResolver;

    public boolean isCurrentPrincipal(UUID principalId) {
        Authentication authentication = authenticationResolver.getCurrentAuthentication();
        return principalRepository.existsByIdAndEmail(principalId, authentication.getName());
    }
}
