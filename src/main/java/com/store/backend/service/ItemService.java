package com.store.backend.service;

import com.store.backend.exception.ItemAlreadyExists;
import com.store.backend.exception.ItemNotEnoughQuantity;
import com.store.backend.exception.ItemNotExistsInShop;
import com.store.backend.data.model.shop.Item;
import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.data.model.shop.ItemQuantityKey;
import com.store.backend.repository.sql.ItemQuantityRepository;
import com.store.backend.repository.sql.ItemRepository;
import com.store.backend.repository.sql.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemQuantityRepository itemQuantityRepository;

    private final ShopRepository shopRepository;

    public Item createItem(Item item) throws ItemAlreadyExists {
        if (itemRepository.existsById(item.getId()))
            throw new ItemAlreadyExists(String.valueOf(item.getId()));
        return itemRepository.save(item);
    }

    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }


    public List<Item> items(){
        return itemRepository.findAll();
    }

    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId).get();
    }

    public void upsertItemQuantity(ItemQuantity itemQuantity) throws ItemNotEnoughQuantity {
        if (itemQuantity.getQuantity() < 0)
            throw new ItemNotEnoughQuantity(itemQuantity.getId().getShopId(), itemQuantity.getItem().getId());
        if (itemQuantity.getQuantity() == 0)
            this.itemQuantityRepository.delete(itemQuantity);
        else {
            itemQuantity.setItem(itemRepository.findById(itemQuantity.getId().getItemId()).get());
            itemQuantity.setShop(shopRepository.findById(itemQuantity.getId().getShopId()).get());
            this.itemQuantityRepository.save(itemQuantity);
        }
    }

    public List<ItemQuantity> fetchShopItems(Long shopId, List<Long> itemIds, boolean mustExists) throws ItemNotExistsInShop {
        List<ItemQuantityKey> itemQuantityKeys = itemIds.stream().map(x -> new ItemQuantityKey(x, shopId)).toList();
        for (ItemQuantityKey itemQuantityKey : itemQuantityKeys) {
            if (mustExists && !this.itemQuantityRepository.existsById(itemQuantityKey))
                throw new ItemNotExistsInShop(itemQuantityKey.getShopId(), itemQuantityKey.getItemId());
        }
        return this.itemQuantityRepository.findAllById(itemQuantityKeys);
    }

    public List<ItemQuantity> fetchShopItems(Long shopId) {
        return this.itemQuantityRepository.findAllByIdShopId(shopId);
    }

    public double itemPrice(Long itemId) {
        return this.itemRepository.findById(itemId).get().getPrice();
    }
}
