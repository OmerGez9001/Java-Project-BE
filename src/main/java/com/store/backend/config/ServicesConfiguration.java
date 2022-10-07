package com.store.backend.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.backend.repository.redis.LoginMetadataRepository;
import com.store.backend.service.LoginService;
import com.store.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfiguration {

    @Value("${login.secret}")
    private String secret;

    @Value("${login.token-expiration-in-min}")
    private Long tokenExpirationInMin;

    @Bean
    public LoginService loginService(LoginMetadataRepository repository, ObjectMapper objectMapper, Utils utils) {
        return new LoginService(repository, objectMapper, utils, secret, tokenExpirationInMin);
    }

}
