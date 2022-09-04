package com.store.backend.data.dto;

import com.store.backend.data.model.shop.Item;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellsPerItemReport {
    private Item item;
    private Long count;
}
