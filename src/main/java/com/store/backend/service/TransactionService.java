package com.store.backend.service;

import com.store.backend.data.dto.TransactionDetails;
import com.store.backend.data.dto.ItemTransactionRequest;
import com.store.backend.data.dto.TransactionResult;
import com.store.backend.exception.ItemNotEnoughQuantity;
import com.store.backend.exception.ItemNotExistsInShop;
import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.data.model.report.ItemLog;
import com.store.backend.data.model.shop.Item;
import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.data.model.shop.ItemQuantityKey;
import com.store.backend.data.model.shop.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = {Throwable.class})
public class TransactionService {
    private final CustomerService customerService;
    private final ItemService itemService;
    private final ItemLogService ItemLogger;

    public TransactionResult buy(TransactionDetails transactionDetails) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        double price = 0;
        String transactionId = UUID.randomUUID().toString();

        AbstractCustomer abstractCustomer = customerService.getCustomer(transactionDetails.getCustomerId());

        List<Long> itemIds = transactionDetails.getItems().stream().map(ItemTransactionRequest::getItemId).toList();

        Map<Long, ItemQuantity> existingItems = itemService.fetchShopItems(transactionDetails.getShopId(), itemIds, true)
                .stream()
                .collect(Collectors.toMap(x -> x.getId().getItemId(), Function.identity()));

        for (ItemTransactionRequest itemTransactionRequest : transactionDetails.getItems()) {

            ItemQuantity existingItem = existingItems.get(itemTransactionRequest.getItemId());

            double itemPrice = itemTransactionRequest.getQuantity() * existingItem.getItem().getPrice();

            price += itemPrice;

            existingItem.setQuantity(existingItem.getQuantity() - itemTransactionRequest.getQuantity());

            itemService.upsertItemQuantity(existingItem);

            ItemLogger.logItem(
                    ItemLog.builder()
                            .transactionId(transactionId)
                            .item(Item.builder().id(itemTransactionRequest.getItemId()).build())
                            .quantity(itemTransactionRequest.getQuantity())
                            .shop(Shop.builder().id(transactionDetails.getShopId()).build())
                            .totalPrice(itemPrice)
                            .category(UUID.randomUUID().toString())
                            .build());
        }
        return new TransactionResult(transactionId, price);
    }

    public TransactionResult sell(TransactionDetails transactionDetails) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        double price = 0;

        String transactionId = UUID.randomUUID().toString();
        AbstractCustomer abstractCustomer = customerService.getCustomer(transactionDetails.getCustomerId());

        List<Long> itemIds = transactionDetails.getItems().stream().map(ItemTransactionRequest::getItemId).toList();

        Map<Long, ItemQuantity> existingItems = itemService.fetchShopItems(transactionDetails.getShopId(), itemIds, false)
                .stream()
                .collect(Collectors.toMap(x -> x.getId().getItemId(), Function.identity()));

        for (ItemTransactionRequest itemTransactionRequest : transactionDetails.getItems()) {

            ItemQuantity existingItem = existingItems.getOrDefault(itemTransactionRequest.getItemId(),
                    ItemQuantity
                            .builder()
                            .id(new ItemQuantityKey(itemTransactionRequest.getItemId(), transactionDetails.getShopId()))
                            .build());

            price += itemTransactionRequest.getQuantity() * this.itemService.itemPrice(existingItem.getId().getItemId());

            existingItem.setQuantity(existingItem.getQuantity() + itemTransactionRequest.getQuantity());

            itemService.upsertItemQuantity(existingItem);

        }
        return new TransactionResult(transactionId, price);
    }
}
