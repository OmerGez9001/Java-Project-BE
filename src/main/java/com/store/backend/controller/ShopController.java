package com.store.backend.controller;

import com.store.backend.assembler.ItemQuantityAssembler;
import com.store.backend.assembler.ShopDtoAssembler;
import com.store.backend.data.dto.ShopDto;
import com.store.backend.data.mapper.ShopMapper;
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

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    private final ShopDtoAssembler shopDtoAssembler;

    private final ItemService itemService;

    private final ItemQuantityAssembler itemQuantityAssembler;

    private final ShopMapper shopMapper;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ShopDto>> getShop(@PathVariable("id") Long id) {
        return ResponseEntity.of(shopService.getShop(id).map(shopMapper::shopToShopDto).map(shopDtoAssembler::toModel));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<CollectionModel<EntityModel<ItemQuantity>>> getShopItems(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemQuantityAssembler.toCollectionModel(itemService.fetchShopItems(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ShopDto>> createShop(ShopDto shopDto) throws ShopAlreadyExists {
        return ResponseEntity.ok(shopDtoAssembler.toModel(shopMapper.shopToShopDto(shopService.createShop(shopMapper.shopDtoToShop(shopDto)))));
    }

    @PutMapping
    public ResponseEntity<EntityModel<ShopDto>> updateShop(ShopDto shopDto) {
        return ResponseEntity.ok(shopDtoAssembler.toModel(shopMapper.shopToShopDto(shopService.updateShop(shopMapper.shopDtoToShop(shopDto)))));
    }

    @GetMapping
    public CollectionModel<EntityModel<ShopDto>> getAllShops() {
        return shopDtoAssembler.toCollectionModel(shopService.shops().stream().map(shopMapper::shopToShopDto).collect(Collectors.toList()));
    }
}
