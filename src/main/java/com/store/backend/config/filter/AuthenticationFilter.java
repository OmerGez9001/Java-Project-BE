package com.store.backend.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.backend.data.model.login.LoginMetadata;
import com.store.backend.repository.redis.LoginMetadataRepository;
import com.store.backend.service.LoginService;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final LoginService loginService;

    private final ObjectMapper objectMapper;

    public AuthenticationFilter(AuthenticationManager authenticationManager, LoginService loginService, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.loginService = loginService;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/api/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String content = IOUtils.toString(request.getReader());
        AuthenticationRequest authenticationRequest = objectMapper.readValue(content, AuthenticationRequest.class);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        loginService.handleSuccessfulLogin(request, response, authResult);
        chain.doFilter(request, response);
    }
}
