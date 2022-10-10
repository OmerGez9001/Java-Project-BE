package com.store.backend.repository.sql;

import com.store.backend.data.model.customer.AbstractCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<AbstractCustomer, String> {
    List<AbstractCustomer> findAllByFullNameLike(String customerNameLike);
}
