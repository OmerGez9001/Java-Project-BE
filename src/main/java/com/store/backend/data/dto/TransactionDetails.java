package com.store.backend.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionDetails {
    private String customerId;
    private Long shopId;
    private List<ItemTransactionRequest> items;

}
