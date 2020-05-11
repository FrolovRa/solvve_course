package com.solvve.course.security;

import com.solvve.course.BaseTest;
import com.solvve.course.domain.Principal;
import org.assertj.core.util.IterableUtil;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class UserDetailServiceImplTest extends BaseTest {

    @Test
    public void testLoadUserByUsername() {
        Principal principal = utils.generateFlatEntityWithoutId(Principal.class);
        principal.setRoles(new ArrayList<>(IterableUtil.toCollection(principalRoleRepository.findAll())));
        principalRepository.save(principal);

        UserDetails userDetails = userDetailService.loadUserByUsername(principal.getEmail());
        assertEquals(userDetails.getUsername(), principal.getEmail());
        assertEquals(userDetails.getPassword(), principal.getPassword());
        assertThat(userDetails.getAuthorities()).extracting("authority")
            .containsExactlyInAnyOrder(principal.getRoles().stream()
                .map(role -> role.getRole().toString())
                .toArray());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testUsernameNotFoundException() {
        userDetailService.loadUserByUsername("wrongEmail");
    }
}