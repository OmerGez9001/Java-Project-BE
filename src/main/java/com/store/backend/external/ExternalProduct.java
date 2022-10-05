package com.store.backend.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalProduct {
    private String id;
    private String title;
    private double price;
    private String category;
    private String description;
}
