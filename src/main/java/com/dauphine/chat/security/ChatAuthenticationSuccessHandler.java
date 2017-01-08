package com.dauphine.chat.security;

import com.dauphine.chat.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author belgacea
 * @date 17/12/2016
 */
@Component
public class ChatAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatAuthenticationSuccessHandler.class);

    private final ObjectMapper mapper;

    @Autowired
    public ChatAuthenticationSuccessHandler(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        ChatUserDetails userDetails = (ChatUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        LOGGER.info(user.getMail() + " is connected ");
        HttpSession session = request.getSession();
        session.setAttribute("username", user.getUsername());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        user.hidePassword();
        mapper.writeValue(writer, user);
        writer.flush();

        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}