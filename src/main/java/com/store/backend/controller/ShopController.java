package com.store.backend.controller;

import com.store.backend.assembler.ItemQuantityAssembler;
import com.store.backend.assembler.ShopDtoAssembler;
import com.store.backend.data.dto.ShopDto;
import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.exception.ShopAlreadyExists;
import com.store.backend.data.model.shop.Shop;
import com.store.backend.service.ItemService;
import com.store.backend.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    private final ShopDtoAssembler shopDtoAssembler;

    private final ItemService itemService;

    private final ItemQuantityAssembler itemQuantityAssembler;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ShopDto>> getShop(@PathVariable("id") Long id) {
        return ResponseEntity.of(shopService.getShop(id).map(ShopDto::new).map(shopDtoAssembler::toModel));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<CollectionModel<EntityModel<ItemQuantity>>> getShopItems(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemQuantityAssembler.toCollectionModel(itemService.fetchShopItems(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ShopDto>> createShop(Shop shop) throws ShopAlreadyExists {
        return ResponseEntity.ok(shopDtoAssembler.toModel(new ShopDto(shopService.createShop(shop))));
    }

    @PutMapping
    public ResponseEntity<EntityModel<ShopDto>> updateShop(Shop shop) {
        return ResponseEntity.ok(shopDtoAssembler.toModel(new ShopDto(shopService.updateShop(shop))));
    }

    @GetMapping
    public CollectionModel<EntityModel<ShopDto>> getAllShops() {
        return shopDtoAssembler.toCollectionModel(shopService.shops().stream().map(ShopDto::new).collect(Collectors.toList()));
    }
}
