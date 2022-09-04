package com.store.backend.data.dto;

import com.store.backend.data.model.shop.Shop;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellsPerShopReport {
    private Shop shop;
    private Long sells;
}
