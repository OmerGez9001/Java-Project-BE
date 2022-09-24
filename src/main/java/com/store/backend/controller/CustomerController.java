package com.store.backend.controller;

import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<AbstractCustomer> getCustomer(@PathVariable("id") String id) {
        return ResponseEntity.of(this.customerService.getCustomer(id));
    }

    @PostMapping
    public ResponseEntity<AbstractCustomer> createCustomer(@RequestBody AbstractCustomer abstractCustomer) {
        return ResponseEntity.ok(customerService.upsertCustomer(abstractCustomer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<AbstractCustomer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.customers());
    }
}
