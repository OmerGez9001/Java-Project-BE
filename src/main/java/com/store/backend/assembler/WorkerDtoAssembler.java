package com.store.backend.assembler;

import com.store.backend.controller.WorkerController;
import com.store.backend.data.dto.WorkerDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WorkerDtoAssembler implements SimpleRepresentationModelAssembler<WorkerDto> {

    @Override
    public void addLinks(EntityModel<WorkerDto> resource) {
        resource.add(linkTo(methodOn(WorkerController.class)
                .getWorker(resource.getContent().getWorkerId())).withSelfRel());
        resource.add(linkTo(methodOn(WorkerController.class).getAllWorkers()).withRel("workers"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<WorkerDto>> resources) {
        resources.add(linkTo(methodOn(WorkerController.class).getAllWorkers()).withSelfRel());
    }
}
