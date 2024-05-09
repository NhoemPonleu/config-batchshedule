package com.example.batchconfig.customer;

import com.example.batchconfig.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Customer findByIdentity(Integer identity);
    Customer findById(Loan id);
}
