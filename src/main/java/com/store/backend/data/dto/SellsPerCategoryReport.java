package com.store.backend.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellsPerCategoryReport {
    private String category;
    private Long count;
}
