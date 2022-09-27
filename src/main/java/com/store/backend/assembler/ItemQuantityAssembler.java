package com.store.backend.assembler;

import com.store.backend.data.model.shop.ItemQuantity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class ItemQuantityAssembler implements SimpleRepresentationModelAssembler<ItemQuantity> {


    @Override
    public void addLinks(EntityModel<ItemQuantity> resource) {

    }

    @Override
    public void addLinks(CollectionModel<EntityModel<ItemQuantity>> resources) {

    }
}
