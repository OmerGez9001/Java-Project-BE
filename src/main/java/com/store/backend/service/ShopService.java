package com.store.backend.service;

import com.store.backend.exception.ShopAlreadyExists;
import com.store.backend.data.model.shop.Shop;
import com.store.backend.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Shop createShop(Shop shop) throws ShopAlreadyExists {
        if (shopRepository.existsById(shop.getId()))
            throw new ShopAlreadyExists("Shop already exists: " + shop.getId());
        return shopRepository.save(shop);
    }
    public Shop getShop(Long shopId){
        return shopRepository.findById(shopId).get();
    }

    public Shop updateShop(Shop shop) {
        return this.shopRepository.save(shop);
    }

    public List<Shop> shops() {
        return this.shopRepository.findAll();
    }
}
