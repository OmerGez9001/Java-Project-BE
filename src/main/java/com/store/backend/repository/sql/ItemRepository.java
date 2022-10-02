package com.store.backend.repository.sql;

import com.store.backend.data.model.shop.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
