package com.solvve.course.security;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Principal;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CurrentPrincipalValidatorTest extends BaseTest {

    @MockBean
    private AuthenticationResolver authenticationResolver;

    @Test
    public void testIsCurrentPrincipal() {
        Principal principal = utils.getPrincipalFromDb();
        when(authenticationResolver.getCurrentAuthentication())
                .thenReturn(new TestingAuthenticationToken(principal.getEmail(), null));

        assertTrue(currentPrincipalValidator.isCurrentPrincipal(principal.getId()));
    }

    @Test
    public void testIsDifferentPrincipal() {
        Principal principal = utils.getPrincipalFromDb();
        Principal secondPrincipal = utils.getPrincipalFromDb();
        when(authenticationResolver.getCurrentAuthentication())
                .thenReturn(new TestingAuthenticationToken(principal.getEmail(), null));

        assertFalse(currentPrincipalValidator.isCurrentPrincipal(secondPrincipal.getId()));
    }
}
