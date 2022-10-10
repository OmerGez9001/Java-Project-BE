package com.store.backend.service;

import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.exception.CustomerException;
import com.store.backend.repository.sql.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public AbstractCustomer createCustomer(AbstractCustomer customer) {
        if (!customerRepository.existsById(customer.getId()))
            return this.customerRepository.save(customer);
        throw new CustomerException("Customer with this id already exists " + customer.getId());
    }

    public AbstractCustomer updateCustomer(AbstractCustomer customer) {
        if (customerRepository.existsById(customer.getId()))
            return this.customerRepository.save(customer);
        throw new CustomerException("Customer with this id is not exists" + customer.getId());
    }

    public Optional<AbstractCustomer> getCustomer(String id) {
        return this.customerRepository.findById(id);
    }

    public List<AbstractCustomer> customersByFullNameLike(String customerName) {
        return this.customerRepository.findAllByFullNameLike("%" + customerName + "%");
    }

    public void deleteCustomer(String customerId) {
        this.customerRepository.deleteById(customerId);
    }

    public List<AbstractCustomer> customers() {
        return this.customerRepository.findAll();
    }
}
