package com.store.backend.controller;

import com.store.backend.assembler.CustomerDtoAssembler;
import com.store.backend.data.dto.CustomerDto;
import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerDtoAssembler customerDtoAssembler;
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDto>> getCustomer(@PathVariable("id") String id) {
        return ResponseEntity.of(this.customerService.getCustomer(id).map(CustomerDto::new).map(customerDtoAssembler::toModel));
    }
    @PostMapping
    public ResponseEntity<EntityModel<CustomerDto>> createCustomer(@RequestBody AbstractCustomer abstractCustomer) {
        return ResponseEntity.ok(customerDtoAssembler.toModel(new CustomerDto(customerService.upsertCustomer(abstractCustomer))));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CustomerDto>>> getAllCustomers() {
        return ResponseEntity.ok(customerDtoAssembler.toCollectionModel(customerService.customers().stream().map(CustomerDto::new).collect(Collectors.toList())));
    }
}
