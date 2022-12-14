package com.store.backend.service;

import com.store.backend.exception.ShopException;
import com.store.backend.data.model.shop.Shop;
import com.store.backend.repository.sql.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Shop createShop(Shop shop) throws ShopException {
        if (shopRepository.existsById(shop.getId()))
            throw new ShopException("Shop is already exists: " + shop.getId());
        return this.shopRepository.save(shop);
    }
    public Optional<Shop> getShop(Long shopId){
        return shopRepository.findById(shopId);
    }

    public Shop updateShop(Shop shop) {
        if (!shopRepository.existsById(shop.getId()))
            throw new ShopException("Shop is not exists: " + shop.getId());
        return this.shopRepository.save(shop);
    }

    public List<Shop> shops() {
        return this.shopRepository.findAll();
    }
}
