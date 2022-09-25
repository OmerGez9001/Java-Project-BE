package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.store.backend.data.model.customer.AbstractCustomer;

@JsonPropertyOrder({"id", "fullName", "phoneNumber", "customerType"})
public record CustomerDto(@JsonIgnore AbstractCustomer abstractCustomer) {
    public String getId() {
        return abstractCustomer.getId();
    }

    public String getFullName() {
        return abstractCustomer.getFullName();
    }

    public String getPhoneNumber() {
        return abstractCustomer.getPhoneNumber();
    }

    public String getCustomerType() {
        return abstractCustomer.getType();
    }
}
