package com.store.backend.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.store.backend.data.model.worker.Job;
import com.store.backend.data.model.worker.Worker;

@JsonPropertyOrder({"workerId", "id", "fullName", "password", "phoneNumber", "accountNumber", "shop", "job"})
public class WorkerDto {
    private final Worker worker;

    public WorkerDto(Worker worker) {
        this.worker = worker;
    }

    public String getWorkerId() {
        return worker.getWorkerId();
    }

    public String getPassword() {
        return worker.getPassword();
    }

    public String getId() {
        return worker.getId();
    }

    public String getFullName() {
        return worker.getFullName();
    }

    public String getPhoneNumber() {
        return worker.getPhoneNumber();
    }

    public String getAccountNumber() {
        return worker.getAccountNumber();
    }

    public Job getJob() {
        return worker.getJob();
    }

    public ShopDto getShop() {
        return new ShopDto(worker.getShop());
    }
}
