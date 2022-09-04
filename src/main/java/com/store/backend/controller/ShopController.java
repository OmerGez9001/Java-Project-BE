package com.store.backend.controller;

import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.exception.ShopAlreadyExists;
import com.store.backend.data.model.shop.Shop;
import com.store.backend.service.ItemService;
import com.store.backend.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    private final ItemService itemService;

    @GetMapping("/{id}")
    public Shop getShop(@PathVariable("id") Long id) {
        return shopService.getShop(id);
    }

    @GetMapping("/{id}/items")
    public List<ItemQuantity> getShopItems(@PathVariable("id") Long id) {
        return itemService.fetchShopItems(id);
    }

    @PostMapping
    public Shop createShop(Shop shop) throws ShopAlreadyExists {
        return shopService.createShop(shop);
    }

    @PutMapping
    public Shop updateShop(Shop shop) {
        return shopService.updateShop(shop);
    }

    @GetMapping
    public List<Shop> getAllShops() {
        return shopService.shops();
    }
}
