package com.example.bankingapi.service;

import com.example.bankingapi.model.Account;
import com.example.bankingapi.model.Transaction;
import com.example.bankingapi.repository.AccountRepository;
import com.example.bankingapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account("Test User", new BigDecimal("1000.00"));
        testAccount.setId(1L);
    }

    @Test
    void testGetAllAccounts() {
        // Arrange
        List<Account> accounts = Arrays.asList(
                testAccount,
                new Account("Another User", new BigDecimal("2000.00"))
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = accountService.getAllAccounts();

        // Assert
        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    void testGetAccountById() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act
        Optional<Account> result = accountService.getAccountById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getName());
        verify(accountRepository).findById(1L);
    }

    @Test
    void testCreateAccount() {
        // Arrange
        Account newAccount = new Account("New User", null);
        Account savedAccount = new Account("New User", BigDecimal.ZERO);
        savedAccount.setId(2L);
        
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        Account result = accountService.createAccount(newAccount);

        // Assert
        assertEquals(2L, result.getId());
        assertEquals("New User", result.getName());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testDeposit() {
        // Arrange
        BigDecimal depositAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("1500.00");
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        Account result = accountService.deposit(1L, depositAmount);

        // Assert
        assertEquals(expectedBalance, result.getBalance());
        verify(accountRepository).findById(1L);
        verify(accountRepository).save(testAccount);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testDepositWithInvalidAmount() {
        // Arrange
        BigDecimal negativeAmount = new BigDecimal("-100.00");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.deposit(1L, negativeAmount);
        });
        
        assertEquals("Deposit amount must be positive", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testWithdraw() {
        // Arrange
        BigDecimal withdrawAmount = new BigDecimal("300.00");
        BigDecimal expectedBalance = new BigDecimal("700.00");
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        Account result = accountService.withdraw(1L, withdrawAmount);

        // Assert
        assertEquals(expectedBalance, result.getBalance());
        verify(accountRepository).findById(1L);
        verify(accountRepository).save(testAccount);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testWithdrawInsufficientFunds() {
        // Arrange
        BigDecimal withdrawAmount = new BigDecimal("1500.00");
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdraw(1L, withdrawAmount);
        });
        
        assertEquals("Insufficient funds", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionHistory() {
        // Arrange
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, new BigDecimal("100.00"), Transaction.TransactionType.DEPOSIT),
                new Transaction(1L, new BigDecimal("50.00"), Transaction.TransactionType.WITHDRAWAL)
        );
        
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccountIdOrderByTimestampDesc(1L)).thenReturn(transactions);

        // Act
        List<Transaction> result = accountService.getTransactionHistory(1L);

        // Assert
        assertEquals(2, result.size());
        verify(accountRepository).existsById(1L);
        verify(transactionRepository).findByAccountIdOrderByTimestampDesc(1L);
    }
} 