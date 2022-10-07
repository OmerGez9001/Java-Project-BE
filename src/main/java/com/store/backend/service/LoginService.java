package com.store.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.backend.config.filter.UserWithClaims;
import com.store.backend.config.filter.WorkerDetails;
import com.store.backend.data.dto.JwtTokenWithId;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoginService {
    private final LoginMetadataRepository repository;

    private final ObjectMapper objectMapper;

    private final Utils utils;

    private final String secret;

    private final Long tokenExpiresInMin;

    public void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        UserWithClaims user = (UserWithClaims) authResult.getPrincipal();
        JwtTokenWithId token = buildJwt(request.getRequestURL().toString(), (UserWithClaims) authResult.getPrincipal());
        repository.save(new LoginMetadata(user.getUsername(), token.getJwtId()));
        objectMapper.writeValue(response.getOutputStream(), token);
    }

    public void handleUserAuthorization(HttpServletRequest request) {
        String token = utils.jwtFromRequest(request);
        DecodedJWT decodedJWT = verifyJwt(token);
        setSecurityContext(decodedJWT);
    }

    private DecodedJWT verifyJwt(String token) {
        JWTVerifier verifier = JWT.require(provideJwtAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String decodedJwtId = decodedJWT.getId();
        String lastJwtId = repository.findById(decodedJWT.getSubject()).orElseThrow(TokenNotFound::new).getJwtID();
        if (!lastJwtId.equals(decodedJwtId))
            throw new NewTokenWasProvided(decodedJWT.getSubject());
        return decodedJWT;
    }

    private JwtTokenWithId buildJwt(String sourceUrl, UserWithClaims user) {
        String jwtId = UUID.randomUUID().toString();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withJWTId(jwtId)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiresInMin * 60 * 1000))
                .withIssuer(sourceUrl)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("shop", user.getShopId())
                .sign(provideJwtAlgorithm());
        return new JwtTokenWithId(accessToken, jwtId);
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

    private Algorithm provideJwtAlgorithm() {
        return Algorithm.HMAC256(secret.getBytes());
    }
}
