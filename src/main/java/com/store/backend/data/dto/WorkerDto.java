package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.store.backend.data.model.worker.Job;
import lombok.Data;


@JsonPropertyOrder({"workerId", "id", "fullName", "password", "phoneNumber", "accountNumber", "shop", "job"})
@Data
public class WorkerDto {
    private String workerId;
    private String password;
    private String id;
    private String fullName;
    private String phoneNumber;
    private String accountNumber;
    private Job job;
    private ShopDto shop;
}
