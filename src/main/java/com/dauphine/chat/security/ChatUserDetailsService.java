package com.dauphine.chat.security;

import java.util.ArrayList;
import java.util.List;

import com.dauphine.chat.domain.User;
import com.dauphine.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByMail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        /*List<String> permissions = userService.getPermissions(user.getMail());
        for (String permission : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission));
        }*/

        return new ChatUserDetails(user, grantedAuthorities);
    }

    public UserDetails loadUserByMail(String mail) throws UsernameNotFoundException {
        User user = userService.findByMail(mail);
        if (user == null) {
            throw new UsernameNotFoundException(mail);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        /*List<String> permissions = userService.getPermissions(user.getMail());
        for (String permission : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission));
        }*/

        return new ChatUserDetails(user, grantedAuthorities);
    }

}
