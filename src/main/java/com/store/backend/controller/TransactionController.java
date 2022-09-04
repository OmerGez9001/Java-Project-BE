package com.store.backend.controller;

import com.store.backend.data.dto.TransactionDetails;
import com.store.backend.data.dto.TransactionResult;
import com.store.backend.exception.ItemNotEnoughQuantity;
import com.store.backend.exception.ItemNotExistsInShop;
import com.store.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/buy")
    public TransactionResult buy(@RequestBody  TransactionDetails transactionDetails) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        return transactionService.buy(transactionDetails);
    }

    @PostMapping("/sell")
    public TransactionResult TransactionResult(@RequestBody  TransactionDetails transactionDetails) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        return transactionService.sell(transactionDetails);
    }

}
