package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.store.backend.data.model.report.TransactionAction;
import lombok.Builder;
import lombok.Value;

@JsonPropertyOrder({"transactionId", "itemName", "shopName", "priceAfterDiscount", "transactionAction"})
@Builder
@Value
public class TransactionLogDto {

    String transactionId;

    String itemName;

    String shopName;

    Long quantity;

    Double priceAfterDiscount;

    TransactionAction transactionAction;
}
