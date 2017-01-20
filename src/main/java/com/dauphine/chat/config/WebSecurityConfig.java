package com.dauphine.chat.config;

import com.dauphine.chat.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author belgacea
 * @date 17/12/2016
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String URL_LOGIN = "/login";



    @Autowired
    private ChatUserDetailsService userDetailsService;
    @Autowired
    private ChatAuthenticationEntryPoint authEntryPoint;
    @Autowired
    private ChatAuthenticationProvider authProvider;
    @Autowired
    private ChatAuthenticationSuccessHandler authSuccessHandler;
    @Autowired
    private ChatAuthenticationFailureHandler authFailureHandler;
    @Autowired
    private ChatLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }

    @Bean
    protected JsonMailPasswordAuthenticationFilter authFilter() throws Exception {
        JsonMailPasswordAuthenticationFilter filter = new JsonMailPasswordAuthenticationFilter(URL_LOGIN, authSuccessHandler, authFailureHandler, objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/js/**", "/lib/**", "/img/**", "/css/**", "/index.html", "/rooms", URL_LOGIN, "/register", "/").permitAll()

                .and()
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests().antMatchers("/chat/", "/chat/**", "/messages/", "/messages/**").permitAll().anyRequest().authenticated();

    }
}
