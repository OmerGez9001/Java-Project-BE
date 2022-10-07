package com.store.backend.utils;

import com.store.backend.exception.AuthorizationHeaderNotFound;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
}
