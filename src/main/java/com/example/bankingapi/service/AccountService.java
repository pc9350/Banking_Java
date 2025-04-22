package com.example.bankingapi.service;

import com.example.bankingapi.model.Account;
import com.example.bankingapi.model.Transaction;
import com.example.bankingapi.repository.AccountRepository;
import com.example.bankingapi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Get all accounts in the system
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Find an account by its ID
     */
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    /**
     * Create a new account
     */
    public Account createAccount(Account account) {
        // Set initial balance to zero if not provided
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    /**
     * Deposit money into an account
     * @param accountId The account ID
     * @param amount The amount to deposit
     * @return The updated account
     */
    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        // Find account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Update balance
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction(
                accountId, 
                amount, 
                Transaction.TransactionType.DEPOSIT
        );
        transactionRepository.save(transaction);

        return account;
    }

    /**
     * Withdraw money from an account
     * @param accountId The account ID
     * @param amount The amount to withdraw
     * @return The updated account
     */
    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount) {
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        // Find account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Check sufficient funds
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Update balance
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction(
                accountId, 
                amount, 
                Transaction.TransactionType.WITHDRAWAL
        );
        transactionRepository.save(transaction);

        return account;
    }

    /**
     * Get transaction history for an account
     * @param accountId The account ID
     * @return List of transactions
     */
    public List<Transaction> getTransactionHistory(Long accountId) {
        // Verify account exists
        if (!accountRepository.existsById(accountId)) {
            throw new RuntimeException("Account not found");
        }
        
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
    }
} 