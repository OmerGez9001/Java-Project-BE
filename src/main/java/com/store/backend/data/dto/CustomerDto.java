package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;


@JsonPropertyOrder({"id", "type", "fullName", "phoneNumber"})
@Builder
@Value
public class CustomerDto {
    String type;
    String id;
    String fullName;
    String phoneNumber;
}
