package com.store.backend.utils;

import com.store.backend.exception.AuthorizationHeaderNotFound;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
public class Utils {

    public String jwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = Optional.ofNullable(request.getHeader(AUTHORIZATION)).orElse(request.getParameter(AUTHORIZATION));
        if (authorizationHeader == null)
            throw new AuthorizationHeaderNotFound("requested path: " + request.getServletPath());
        String token = authorizationHeader.substring("Bearer ".length());
        return token;
    }

    public String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(Principal::getName).orElse("admin");
    }
}
