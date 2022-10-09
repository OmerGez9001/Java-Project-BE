package com.store.backend.controller;

import com.store.backend.data.dto.ItemsTransactionAggregation;
import com.store.backend.data.dto.TransactionLogDto;
import com.store.backend.data.mapper.TransactionLogMapper;
import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.data.model.report.RegisterType;
import com.store.backend.service.ItemLogService;
import com.store.backend.service.RegisterLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ItemLogService itemLogService;

    private final RegisterLogService registerLogService;

    private final TransactionLogMapper transactionLogMapper;

    @GetMapping("/items")
    public ResponseEntity<List<ItemsTransactionAggregation>> getTransactedItems() {
        return ResponseEntity.ok(itemLogService.getTransactedItems());
    }

    @GetMapping("/items/worker/{id}")
    public ResponseEntity<CompletableFuture<List<TransactionLogDto>>> getTransactedItemsByWorkerId(
            @PathVariable("id") String workerId,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size) {
        return ResponseEntity.ok(itemLogService.getWorkerTransactedItems(workerId, page, size).thenApply(transactionLogMapper::transactionLogToTransactionLog));
    }

    @GetMapping("/items/customer/{id}")
    public ResponseEntity<CompletableFuture<List<TransactionLogDto>>> getTransactedItemsByCustomerId(
            @PathVariable("id") String workerId,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size) {
        return ResponseEntity.ok(itemLogService.getCustomerTransactedItems(workerId, page, size).thenApply(transactionLogMapper::transactionLogToTransactionLog));
    }

    @GetMapping("/customer")
    public ResponseEntity<List<RegisterLog>> getAllCustomerLog() {
        return ResponseEntity.ok(registerLogService.getAllByRegisterType(RegisterType.CUSTOMER));
    }

    @GetMapping("/worker")
    public ResponseEntity<List<RegisterLog>> getAllWorkersLog() {
        return ResponseEntity.ok(registerLogService.getAllByRegisterType(RegisterType.WORKER));
    }

}
