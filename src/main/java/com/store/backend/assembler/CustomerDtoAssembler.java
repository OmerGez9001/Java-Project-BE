package com.store.backend.assembler;

import com.store.backend.controller.CustomerController;
import com.store.backend.data.dto.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoAssembler extends SimpleIdentifiableRepresentationModelAssembler<CustomerDto> {
    public CustomerDtoAssembler() {
        super(CustomerController.class);
    }
}
