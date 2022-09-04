package com.store.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResult {
    private String transactionId;
    private double sum;
}
