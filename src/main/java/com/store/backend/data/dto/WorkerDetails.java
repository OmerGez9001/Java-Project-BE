package com.store.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerDetails {
    private String workerId;
    private Long shopId;

}
