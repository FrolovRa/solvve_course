package com.solvve.course.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR') or "
        + "(hasAnyAuthority('USER') and @currentPrincipalValidator.isCurrentPrincipal(#id))")
public @interface AdminModeratorOrCurrentUser {
}
