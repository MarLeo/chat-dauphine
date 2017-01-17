package com.dauphine.chat.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author belgacea
 * @date 17/12/2016
 */
@Component
public class ChatAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    public ChatAuthenticationEntryPoint() {
        super();
        setRealmName("Authentication required");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
