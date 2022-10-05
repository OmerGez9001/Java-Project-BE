package com.store.backend.external;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExternalProductsApi {
    private final RestTemplate restTemplate;

    public List<ExternalProduct> fetchProducts() {
        return restTemplate.exchange("https://fakestoreapi.com/products",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<ExternalProduct>>() {
                        })
                .getBody();
    }
}
