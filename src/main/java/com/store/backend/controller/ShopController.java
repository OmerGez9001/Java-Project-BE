package com.store.backend.controller;

import com.store.backend.exception.ShopAlreadyExists;
import com.store.backend.data.model.shop.Shop;
import com.store.backend.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    public Shop createShop(Shop shop) throws ShopAlreadyExists {
        return shopService.createShop(shop);
    }

    @PutMapping
    public Shop updateShop(Shop shop) {
        return shopService.updateShop(shop);
    }

    @GetMapping
    public List<Shop> getAllShops(){
        return shopService.shops();
    }
}
