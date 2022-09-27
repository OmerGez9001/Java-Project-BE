package com.store.backend.assembler;

import com.store.backend.controller.ShopController;
import com.store.backend.data.dto.ShopDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ShopDtoAssembler implements SimpleRepresentationModelAssembler<ShopDto> {

    @Override
    public void addLinks(EntityModel<ShopDto> resource) {
        resource.add(linkTo(methodOn(ShopController.class)
                .getShopItems(resource.getContent().getId())).withRel("items"));
        resource.add(linkTo(methodOn(ShopController.class)
                .getShop(resource.getContent().getId())).withSelfRel());
        resource.add(linkTo(methodOn(ShopController.class).getAllShops()).withRel("shops"));

    }

    @Override
    public void addLinks(CollectionModel<EntityModel<ShopDto>> resources) {
        resources.add(linkTo(methodOn(ShopController.class).getAllShops()).withSelfRel());
    }
}
