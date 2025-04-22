package com.example.bankingapi.repository;

import com.example.bankingapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Custom query method to find transactions by account ID
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
} 