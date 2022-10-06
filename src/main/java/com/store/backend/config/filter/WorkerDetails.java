package com.store.backend.config.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerDetails {
    private String workerId;
    private Long shopId;

}
