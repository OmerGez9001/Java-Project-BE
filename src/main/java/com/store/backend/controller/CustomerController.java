package com.store.backend.controller;

import com.store.backend.exception.CustomerAlreadyExists;
import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public AbstractCustomer createCustomer(@RequestBody AbstractCustomer abstractCustomer) throws CustomerAlreadyExists {
        return customerService.createCustomer(abstractCustomer);
    }

    @PutMapping
    public AbstractCustomer updateCustomer(@RequestBody AbstractCustomer abstractCustomer){
        return customerService.updateCustomer(abstractCustomer);

    }

    @GetMapping
    public List<AbstractCustomer> getAllCustomers(){
        return customerService.customers();
    }
}
