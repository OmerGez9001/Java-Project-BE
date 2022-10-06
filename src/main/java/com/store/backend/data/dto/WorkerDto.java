package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.store.backend.data.model.worker.Job;
import lombok.Builder;
import lombok.Data;
import lombok.Value;


@JsonPropertyOrder({"workerId", "id", "fullName", "password", "phoneNumber", "accountNumber", "shop", "job"})
@Builder
@Value
public class WorkerDto {
    String workerId;
    String password;
    String id;
    String fullName;
    String phoneNumber;
    String accountNumber;
    Job job;
    ShopDto shop;
}
