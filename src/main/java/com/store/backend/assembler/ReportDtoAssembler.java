package com.store.backend.assembler;

import com.store.backend.controller.ReportController;
import com.store.backend.data.dto.TransactionLogDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReportDtoAssembler implements SimpleRepresentationModelAssembler<TransactionLogDto> {

    @Override
    public void addLinks(EntityModel<TransactionLogDto> resource) {
        resource.add(linkTo(methodOn(ReportController.class)
                .getTransactedItemsByCustomerId(resource.getContent().getPerformedOn(), Optional.empty(), Optional.empty())).withRel("customerId"));
        resource.add(linkTo(methodOn(ReportController.class)
                .getTransactedItemsByWorkerId(resource.getContent().getPerformedBy(), Optional.empty(), Optional.empty())).withRel("workerId"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<TransactionLogDto>> resources) {}
}
