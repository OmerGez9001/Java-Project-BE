package com.store.backend.controller;

import com.store.backend.data.dto.TransactionDetails;
import com.store.backend.data.dto.TransactionResult;
import com.store.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/buy")
    @SneakyThrows

    public ResponseEntity<TransactionResult> buy(@RequestBody TransactionDetails transactionDetails) {
        return ResponseEntity.ok(transactionService.buy(transactionDetails));
    }

    @PostMapping("/sell")
    @SneakyThrows
    public ResponseEntity<TransactionResult> sell(@RequestBody TransactionDetails transactionDetails) {
        return ResponseEntity.ok(transactionService.sell(transactionDetails));
    }

}
