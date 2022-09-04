package com.store.backend.service;

import com.store.backend.exception.CustomerAlreadyExists;
import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public AbstractCustomer createCustomer(AbstractCustomer customer) throws CustomerAlreadyExists {
        if (customerRepository.existsById(customer.getId())) {
            throw new CustomerAlreadyExists("Customer already exists: " + customer.getId());
        }
        return this.customerRepository.save(customer);
    }

    public AbstractCustomer updateCustomer(AbstractCustomer customer) {
        return this.customerRepository.save(customer);

    }

    public AbstractCustomer getCustomer(String id){
        return this.customerRepository.findById(id).get();
    }

    public List<AbstractCustomer> customers() {
        return this.customerRepository.findAll();
    }
}
