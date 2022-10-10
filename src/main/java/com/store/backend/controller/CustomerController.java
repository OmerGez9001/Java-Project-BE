package com.store.backend.controller;

import com.store.backend.assembler.CustomerDtoAssembler;
import com.store.backend.data.dto.CustomerDto;
import com.store.backend.data.mapper.CustomerMapper;
import com.store.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerDtoAssembler customerDtoAssembler;
    private final CustomerMapper customerMapper;

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CustomerDto>> getCustomer(@PathVariable("id") String id) {
        Optional<CustomerDto> fetchedCustomer = this.customerService
                .getCustomer(id)
                .map(customerMapper::customerToCustomerDto);
        return ResponseEntity.of(fetchedCustomer.map(customerDtoAssembler::toModel));
    }

    @PostMapping
    public ResponseEntity<EntityModel<CustomerDto>> createCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = customerMapper.customerToCustomerDto(customerService.createCustomer(customerMapper.customerDtoToCustomer(customerDto)));
        return ResponseEntity.ok(customerDtoAssembler.toModel(createdCustomer));
    }
    @PutMapping
    public ResponseEntity<EntityModel<CustomerDto>> updateCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = customerMapper.customerToCustomerDto(customerService.updateCustomer(customerMapper.customerDtoToCustomer(customerDto)));
        return ResponseEntity.ok(customerDtoAssembler.toModel(createdCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CustomerDto>>> getAllCustomers() {
        List<CustomerDto> fetchedCustomers = customerService
                .customers()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .toList();
        return ResponseEntity.ok(customerDtoAssembler.toCollectionModel(fetchedCustomers));
    }

    @GetMapping("fullname/{name}")
    public ResponseEntity<CollectionModel<EntityModel<CustomerDto>>> getAllCustomersLike(@PathVariable String name) {
        List<CustomerDto> fetchedCustomers = customerService
                .customersByFullNameLike(name)
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .toList();
        return ResponseEntity.ok(customerDtoAssembler.toCollectionModel(fetchedCustomers));
    }
}
