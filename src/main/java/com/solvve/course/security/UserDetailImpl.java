package com.solvve.course.security;

import com.solvve.course.domain.Principal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

public class UserDetailImpl extends User {

    public UserDetailImpl(Principal principal) {
        super(principal.getEmail(), principal.getPassword(),
                principal.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r.getRole().toString()))
                        .collect(Collectors.toList()));
    }
}
