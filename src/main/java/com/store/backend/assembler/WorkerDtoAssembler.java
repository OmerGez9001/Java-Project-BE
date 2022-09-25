package com.store.backend.assembler;

import com.store.backend.controller.WorkerController;
import com.store.backend.data.dto.WorkerDto;

public class WorkerDtoAssembler extends SimpleIdentifiableRepresentationModelAssembler<WorkerDto> {
    public WorkerDtoAssembler() {
        super(WorkerController.class);
    }
}
