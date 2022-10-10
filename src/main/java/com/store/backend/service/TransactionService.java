package com.store.backend.service;

import com.store.backend.data.dto.TransactionDetails;
import com.store.backend.data.dto.ItemTransactionRequest;
import com.store.backend.data.dto.TransactionResult;
import com.store.backend.data.model.report.TransactionAction;
import com.store.backend.data.model.report.TransactionLog;
import com.store.backend.exception.ItemNotEnoughQuantity;
import com.store.backend.exception.ItemNotExistsInShop;
import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.data.model.shop.ItemQuantityKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = {Throwable.class})
public class TransactionService {
    private final CustomerService customerService;
    private final ItemService itemService;
    private final ShopService shopService;
    private final ItemLogService itemLogService;

    public TransactionResult buy(TransactionDetails request) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        double price = 0;
        String transactionId = UUID.randomUUID().toString();

        AbstractCustomer abstractCustomer = customerService.getCustomer(request.getCustomerId()).get();

        List<Long> itemIds = request.getItems().stream().map(ItemTransactionRequest::getItemId).toList();

        Map<Long, ItemQuantity> existingItems = itemService.fetchShopItems(request.getShopId(), itemIds, true)
                .stream()
                .collect(Collectors.toMap(x -> x.getId().getItemId(), Function.identity()));

        List<TransactionLog> transactionLogs = new ArrayList<>();

        for (ItemTransactionRequest item : request.getItems()) {

            ItemQuantity existingItem = existingItems.get(item.getItemId());

            double itemPrice =  abstractCustomer.buyDiscount(item.getQuantity() * existingItem.getItem().getPrice());

            price += itemPrice;

            existingItem.setQuantity(existingItem.getQuantity() - item.getQuantity());

            itemService.upsertItemQuantity(existingItem);

            TransactionLog transactionLog = TransactionLog.builder()
                    .transactionId(transactionId)
                    .itemName(itemService.getItem(item.getItemId()).getName())
                    .quantity(item.getQuantity())
                    .shopName(shopService.getShop(request.getShopId()).get().getShopName())
                    .priceAfterDiscount(itemPrice)
                    .performedOn(request.getCustomerId())
                    .transactionAction(TransactionAction.BUY)
                    .build();
            transactionLogs.add(transactionLog);
        }

        itemLogService.logItems(transactionLogs);

        return new TransactionResult(transactionId, price);
    }

    public TransactionResult sell(TransactionDetails request) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        double price = 0;

        String transactionId = UUID.randomUUID().toString();
        AbstractCustomer abstractCustomer = customerService.getCustomer(request.getCustomerId()).get();

        List<Long> itemIds = request.getItems().stream().map(ItemTransactionRequest::getItemId).toList();

        Map<Long, ItemQuantity> existingItems = itemService.fetchShopItems(request.getShopId(), itemIds, false)
                .stream()
                .collect(Collectors.toMap(x -> x.getId().getItemId(), Function.identity()));
        List<TransactionLog> transactionLogs = new ArrayList<>();

        for (ItemTransactionRequest item : request.getItems()) {

            ItemQuantity existingItem = existingItems.getOrDefault(item.getItemId(),
                    ItemQuantity
                            .builder()
                            .id(new ItemQuantityKey(item.getItemId(), request.getShopId()))
                            .build());

            double itemPrice =  abstractCustomer.sellDiscount(item.getQuantity() * this.itemService.itemPrice(existingItem.getId().getItemId()));

            price += itemPrice;

            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());

            itemService.upsertItemQuantity(existingItem);

            TransactionLog transactionLog = TransactionLog.builder()
                    .transactionId(transactionId)
                    .itemName(itemService.getItem(item.getItemId()).getName())
                    .quantity(item.getQuantity())
                    .shopName(shopService.getShop(request.getShopId()).get().getShopName())
                    .priceAfterDiscount(itemPrice)
                    .performedOn(request.getCustomerId())
                    .transactionAction(TransactionAction.SELL)
                    .build();
            transactionLogs.add(transactionLog);
        }

        itemLogService.logItems(transactionLogs);

        return new TransactionResult(transactionId, price);
    }
}
