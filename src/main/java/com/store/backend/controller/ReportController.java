package com.store.backend.controller;

import com.store.backend.assembler.ReportDtoAssembler;
import com.store.backend.data.dto.ItemsTransactionAggregation;
import com.store.backend.data.dto.TransactionLogDto;
import com.store.backend.data.mapper.TransactionLogMapper;
import com.store.backend.service.ItemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

    private final TransactionLogMapper transactionLogMapper;

    private final ReportDtoAssembler reportDtoAssembler;

    @GetMapping("/items")
    public ResponseEntity<List<ItemsTransactionAggregation>> getTransactedItems(@RequestParam(value="quantity",required = false)Optional<Long> quantity) {
        return ResponseEntity.ok(itemLogService.getTransactedItems(quantity));
    }

    @GetMapping("/items/worker/{id}")
    public ResponseEntity<CompletableFuture<CollectionModel<EntityModel<TransactionLogDto>>>> getTransactedItemsByWorkerId(
            @PathVariable("id") String workerId,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size) {
        return ResponseEntity.ok(itemLogService.getWorkerTransactedItems(workerId, page, size)
                .thenApply(transactionLogMapper::transactionLogToTransactionLog)
                .thenApply(reportDtoAssembler::toCollectionModel));
    }

    @GetMapping("/items/customer/{id}")
    public ResponseEntity<CompletableFuture<CollectionModel<EntityModel<TransactionLogDto>>>> getTransactedItemsByCustomerId(
            @PathVariable("id") String workerId,
            @RequestParam(value = "page", required = false) Optional<Integer> page,
            @RequestParam(value = "size", required = false) Optional<Integer> size) {
        return ResponseEntity.ok(itemLogService.getCustomerTransactedItems(workerId, page, size)
                .thenApply(transactionLogMapper::transactionLogToTransactionLog)
                .thenApply(reportDtoAssembler::toCollectionModel));
    }

}
