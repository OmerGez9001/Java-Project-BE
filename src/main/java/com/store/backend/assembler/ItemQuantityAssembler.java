package com.store.backend.assembler;

import com.store.backend.controller.ShopController;
import com.store.backend.controller.TransactionController;
import com.store.backend.data.dto.TransactionDetails;
import com.store.backend.data.model.shop.ItemQuantity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ItemQuantityAssembler implements SimpleRepresentationModelAssembler<ItemQuantity> {


    @Override
    public void addLinks(EntityModel<ItemQuantity> resource) {

    }

    @Override
    public void addLinks(CollectionModel<EntityModel<ItemQuantity>> resources) {
        ItemQuantity itemQuantity = resources.getContent().stream().findAny().get().getContent();

        resources.add(linkTo(methodOn(ShopController.class)
                .getShopItems(itemQuantity.getId().getShopId())).withSelfRel());
        resources.add(linkTo(methodOn(ShopController.class).getAllShops()).withRel("shops"));
        resources.add(linkTo(methodOn(TransactionController.class).sell(new TransactionDetails())).withRel("sell"));
        resources.add(linkTo(methodOn(TransactionController.class).buy(new TransactionDetails())).withRel("buy"));


    }
}
