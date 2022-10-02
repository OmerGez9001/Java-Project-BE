package com.store.backend.repository.sql;

import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.data.model.shop.ItemQuantityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemQuantityRepository extends JpaRepository<ItemQuantity, ItemQuantityKey> {
    List<ItemQuantity> findAllByIdShopId(Long shopId);
}
