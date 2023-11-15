package com.app.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JWTAuthenticationFilter extends AuthenticationFilter {

    public JWTAuthenticationFilter() {
        super(new JWTAuthenticationManager(), new JWTAuthenticationConverter());

        setSuccessHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }

    static class JWTAuthenticationConverter implements AuthenticationConverter {

        @Override
        public Authentication convert(HttpServletRequest request) {
            if (request.getHeader("Authorization") != null) {
                var splitValue = request.getHeader("Authorization").split(" ");
                if (splitValue.length != 2) {
                    return null;
                }
                String token = splitValue[1];
                return new JWTAuthentication(token);
            }
            return null;
        }
    }

    static class JWTAuthenticationManager implements AuthenticationManager {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            JWTService jwtService = new JWTService();
            if (authentication instanceof JWTAuthentication jwtAuthentication) {
                String token = jwtAuthentication.getCredentials();

                if (token != null) {
                    String username;
                    List<String> roles = new ArrayList<>();
                        try {
                        username = jwtService.getUserNameFromJWT(token);
                        roles = jwtService.getRolesFromJWT(token);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    jwtAuthentication.setUsername(username);
                    jwtAuthentication.setRoleList(roles);
                    return jwtAuthentication;
                }
            }
            return null;
        }
    }
}
