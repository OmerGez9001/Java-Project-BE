package com.store.backend.controller;

import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.exception.ShopAlreadyExists;
import com.store.backend.data.model.shop.Shop;
import com.store.backend.service.ItemService;
import com.store.backend.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShop(@PathVariable("id") Long id) {
        return ResponseEntity.of(shopService.getShop(id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemQuantity>> getShopItems(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemService.fetchShopItems(id));
    }

    @PostMapping
    public ResponseEntity<Shop> createShop(Shop shop) throws ShopAlreadyExists {
        return ResponseEntity.ok(shopService.createShop(shop));
    }

    @PutMapping
    public ResponseEntity<Shop> updateShop(Shop shop) {
        return ResponseEntity.ok(shopService.updateShop(shop));
    }

    @GetMapping
    public List<Shop> getAllShops() {
        return shopService.shops();
    }
}
