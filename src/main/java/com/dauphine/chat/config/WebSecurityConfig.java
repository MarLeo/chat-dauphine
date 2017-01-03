package com.dauphine.chat.config;

import com.dauphine.chat.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;*/


/**
 * @author belgacea
 * @date 17/12/2016
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String URL_LOGIN = "/login";

//    private static final String MAIL = "mail";
//    private static final String PASSWORD = "password";

    //private static final String SECURE_ADMIN_PASSWORD = "rockandroll";

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
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }

    @Bean
    protected JsonMailPasswordAuthenticationFilter authFilter() throws Exception {
        JsonMailPasswordAuthenticationFilter filter = new JsonMailPasswordAuthenticationFilter(URL_LOGIN, authSuccessHandler, authFailureHandler, objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        ChatAuthenticationProvider authenticationProvider = new ChatAuthenticationProvider(userDetailsService, passwordEncoder());
////        authenticationProvider.setUserDetailsService(userDetailsService);
////        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        /*auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());*/
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
//                .loginProcessingUrl(URL_LOGIN)
//                .usernameParameter(MAIL)
//                .passwordParameter(PASSWORD)
//                .successHandler(authSuccessHandler)
//                .failureHandler(authFailureHandler)
//                .and()
//                .antMatchers("/websocket").hasRole("ADMIN")
//                .logout()
//                .permitAll()
//                .logoutRequestMatcher(new AntPathRequestMatcher(URL_LOGIN, "DELETE"))
//                .logoutSuccessHandler(logoutSuccessHandler)
//                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/js/**", "/lib/**", "/img/**", "/css/**", "/index.html", URL_LOGIN, "/register", "/").permitAll()

                .and()
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests().anyRequest().authenticated();

    }

    /*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(new AuthenticationProvider() {

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

                List<GrantedAuthority> authorities = SECURE_ADMIN_PASSWORD.equals(token.getCredentials()) ?
                        AuthorityUtils.createAuthorityList("ROLE_ADMIN") : null;

                return new UsernamePasswordAuthenticationToken(token.getName(), token.getCredentials(), authorities);
            }
        });
    }
    */

}