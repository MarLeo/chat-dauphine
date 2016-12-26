package com.dauphine.chat.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author belgacea
 * @date 26/12/2016
 */
@Component
public class SecurityContextService {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void setAuthentication(final Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

