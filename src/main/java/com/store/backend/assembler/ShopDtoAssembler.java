package com.store.backend.assembler;

import com.store.backend.controller.ShopController;
import com.store.backend.data.dto.ShopDto;

public class ShopDtoAssembler extends SimpleIdentifiableRepresentationModelAssembler<ShopDto> {

    public ShopDtoAssembler() {
        super(ShopController.class);
    }
}
