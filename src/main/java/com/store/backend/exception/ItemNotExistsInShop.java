package com.store.backend.exception;

public class ItemNotExistsInShop extends RuntimeException {
    public ItemNotExistsInShop(Long shopId, Long itemId) {
        super("Item: %s is not exists in shop: %s".formatted(itemId, shopId));
    }
}
