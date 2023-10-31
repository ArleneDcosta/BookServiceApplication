package com.books.bookmarketplace.service;


import com.books.bookmarketplace.entity.Book;
import com.books.bookmarketplace.entity.Transaction;
import com.books.bookmarketplace.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;


    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        return transactionOptional.orElse(null);
    }
    @Override
    public Transaction addTransaction(Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }

    public Transaction updateTransaction(Transaction newTransaction, Long id) {
        try {
            Transaction existingTransaction = transactionRepository.findById(id).orElse(null);
            if (existingTransaction != null) {
                existingTransaction.setUser(newTransaction.getUser());
                existingTransaction.setBook(newTransaction.getBook());
                existingTransaction.setTransactionDate(new Date()); // Set the current date as the transaction date
                existingTransaction.setTransactionAmount(newTransaction.getTransactionAmount()); // Set the initial amount to 0 or any appropriate default
                existingTransaction.setStatus(newTransaction.getStatus()); // Set an initial status
                return transactionRepository.save(existingTransaction);
            } else {
                throw new IllegalArgumentException("Transaction not found for the given ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTransaction(Long id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isPresent()) {
            transactionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Book not found for the given ID: " + id);
        }
    }


}
