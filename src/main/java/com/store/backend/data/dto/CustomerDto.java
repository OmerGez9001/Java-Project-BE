package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


@JsonPropertyOrder({"id", "type", "fullName", "phoneNumber"})
@Data
public class CustomerDto {
    private String type;
    private String id;
    private String fullName;
    private String phoneNumber;
}
