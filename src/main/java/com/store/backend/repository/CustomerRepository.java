package com.store.backend.repository;

import com.store.backend.data.model.customer.AbstractCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<AbstractCustomer,String> {
}
