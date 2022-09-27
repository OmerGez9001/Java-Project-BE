package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.store.backend.data.model.shop.Shop;

@JsonPropertyOrder({"id", "shopName"})

public record ShopDto(@JsonIgnore Shop shop) {
    public long getId() {
        return shop.getId();
    }

    public String getShopName() {
        return shop.getShopName();
    }
}
