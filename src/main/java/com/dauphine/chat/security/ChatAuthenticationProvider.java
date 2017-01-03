package com.dauphine.chat.security;

import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author belgacea
 * @date 27/12/2016
 */
@Component
public class ChatAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LogManager.getLogger(ChatAuthenticationProvider.class);

    private ChatUserDetailsService userDetailsService;
    private PasswordEncoder encoder;

    public ChatAuthenticationProvider(final ChatUserDetailsService userDetailsService, final PasswordEncoder encoder) {
        super();
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String mail = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        try {
            ChatUserDetails userDetails = userDetailsService.loadUserByMail(mail);
            if (!encoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
            }
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            //if (authorities == null) throw new InsufficientAuthenticationException("User has no roles assigned");
            //authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("User not found : " + mail);
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}