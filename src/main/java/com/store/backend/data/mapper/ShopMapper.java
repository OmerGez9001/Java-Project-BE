package com.store.backend.data.mapper;

import com.store.backend.data.dto.ShopDto;
import com.store.backend.data.model.shop.Shop;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    ShopDto shopToShopDto(Shop shop);

    Shop shopDtoToShop(ShopDto shopDto);
}
