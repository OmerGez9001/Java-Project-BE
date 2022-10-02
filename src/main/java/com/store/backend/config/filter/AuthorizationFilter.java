package com.store.backend.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.store.backend.data.dto.WorkerDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getServletPath());
        if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION) != null ? request.getHeader(AUTHORIZATION) : request.getParameter("Authorization");
            String token = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            Long shopId = decodedJWT.getClaims().get("shop").as(Long.class);
            Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            usernamePasswordAuthenticationToken.setDetails(new WorkerDetails(username, shopId));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        }
    }
}
