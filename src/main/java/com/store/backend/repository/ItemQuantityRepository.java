package com.store.backend.repository;

import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.data.model.shop.ItemQuantityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemQuantityRepository extends JpaRepository<ItemQuantity, ItemQuantityKey> {
}
