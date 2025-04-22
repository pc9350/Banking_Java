package com.example.bankingapi.repository;

import com.example.bankingapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Spring Data JPA automatically implements basic CRUD operations
    // No need to write any additional code for standard operations
} 