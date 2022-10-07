package com.store.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.backend.config.filter.UserWithClaims;
import com.store.backend.config.filter.WorkerDetails;
import com.store.backend.data.dto.JwtToken;
import com.store.backend.data.model.login.LoginMetadata;
import com.store.backend.exception.NewTokenWasProvided;
import com.store.backend.exception.TokenNotFound;
import com.store.backend.repository.redis.LoginMetadataRepository;
import com.store.backend.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class LoginService {
    private final LoginMetadataRepository repository;

    private final ObjectMapper objectMapper;

    private final Utils utils;

    private final String secret;

    public void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        UserWithClaims user = (UserWithClaims) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String jwtId = UUID.randomUUID().toString();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withJWTId(jwtId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("shop", user.getShopId())
                .sign(algorithm);
        response.setContentType(APPLICATION_JSON_VALUE);
        repository.save(new LoginMetadata(user.getUsername(), jwtId));
        objectMapper.writeValue(response.getOutputStream(), new JwtToken(accessToken));
    }

    public void handleUserAuthorization(HttpServletRequest request) {

        String token = utils.jwtFromRequest(request);
        DecodedJWT decodedJWT = verifyJwt(token);
        setSecurityContext(decodedJWT);
    }

    private DecodedJWT verifyJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String decodedJwtId = decodedJWT.getId();
        String lastJwtId = repository.findById(decodedJWT.getSubject()).orElseThrow(TokenNotFound::new).getJwtID();
        if (!lastJwtId.equals(decodedJwtId))
            throw new NewTokenWasProvided(decodedJWT.getSubject());
        return decodedJWT;
    }

    private void setSecurityContext(DecodedJWT decodedJWT) {
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Long shopId = decodedJWT.getClaims().get("shop").as(Long.class);
        Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        usernamePasswordAuthenticationToken.setDetails(new WorkerDetails(username, shopId));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
