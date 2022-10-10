package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.store.backend.data.model.report.TransactionAction;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@JsonPropertyOrder({"transactionId", "itemName", "shopName", "priceAfterDiscount", "transactionAction", "performedOn", "performedBy", "creationTime"})
@Builder
@Value
public class TransactionLogDto {

    String transactionId;

    String itemName;

    String shopName;

    Long quantity;

    Double priceAfterDiscount;

    TransactionAction transactionAction;

    String performedBy;

    String performedOn;

    Date creationTime;
}
