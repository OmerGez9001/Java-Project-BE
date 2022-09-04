package com.store.backend.exception;

public class ItemNotEnoughQuantity extends Exception {
    public ItemNotEnoughQuantity(Long shopId, Long itemId) {
        super("In shop id: %s with item id: %s ".formatted(shopId, itemId));
    }
}
