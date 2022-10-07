package com.store.backend.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
public class Utils {

    public String jwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = Optional.ofNullable(request.getHeader(AUTHORIZATION)).orElse(request.getParameter("Authorization"));
        String token = authorizationHeader.substring("Bearer ".length());
        return token;
    }
}
