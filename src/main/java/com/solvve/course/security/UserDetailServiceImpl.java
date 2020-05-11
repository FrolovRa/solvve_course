package com.solvve.course.security;

import com.solvve.course.domain.Principal;
import com.solvve.course.repository.PrincipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private PrincipalRepository principalRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Principal principal = principalRepository.findByEmail(email);
        if (principal == null) {
            throw new UsernameNotFoundException("User " + email + " is not found!");
        }
        return new UserDetailImpl(principal);
    }
}
