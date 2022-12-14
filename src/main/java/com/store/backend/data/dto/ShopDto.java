package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonPropertyOrder({"id", "shopName"})
@Builder
@Value
public class ShopDto {
    Long id;
    String shopName;
}
