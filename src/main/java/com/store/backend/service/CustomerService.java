package com.store.backend.service;

import com.store.backend.data.model.report.RegisterAction;
import com.store.backend.data.model.customer.AbstractCustomer;
import com.store.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RegisterLogService registerLogService;

    public AbstractCustomer upsertCustomer(AbstractCustomer customer) {
        AbstractCustomer persistedCustomer = this.customerRepository.save(customer);
        RegisterAction registerAction = this.customerRepository.existsById(persistedCustomer.getId()) ? RegisterAction.MODIFY : RegisterAction.CREATE;
        registerLogService.registerCustomerLog(persistedCustomer.getId(), registerAction);
        return persistedCustomer;

    }

    public Optional<AbstractCustomer> getCustomer(String id) {
        return this.customerRepository.findById(id);
    }

    public void deleteCustomer(String customerId) {
        this.customerRepository.deleteById(customerId);
        this.registerLogService.registerCustomerLog(customerId, RegisterAction.DELETE);
    }

    public List<AbstractCustomer> customers() {
        return this.customerRepository.findAll();
    }
}
