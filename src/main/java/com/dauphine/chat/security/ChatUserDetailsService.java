package com.dauphine.chat.security;

import com.dauphine.chat.domain.User;
import com.dauphine.chat.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author belgacea
 * @date 17/12/2016
 */
@Service
public class ChatUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public ChatUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    public ChatUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByMail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        return new ChatUserDetails(user, grantedAuthorities);
    }

    public ChatUserDetails loadUserByMail(String mail) throws UsernameNotFoundException {
        User user = userService.findByMail(mail);
        if (user == null) {
            throw new UsernameNotFoundException(mail);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        return new ChatUserDetails(user, grantedAuthorities);
    }

    public ChatUserDetails loadUserByMailPassword(String mail, String password) throws UsernameNotFoundException {
        User user = userService.findByMailPassword(mail, password);
        if (user == null) {
            throw new UsernameNotFoundException(mail);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        return new ChatUserDetails(user, grantedAuthorities);
    }

}
