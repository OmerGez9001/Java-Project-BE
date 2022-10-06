package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({"id", "shopName"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private long id;
    private String shopName;
}
