package com.books.bookmarketplace.service;

import com.books.bookmarketplace.entity.Book;
import com.books.bookmarketplace.entity.Transaction;
import com.books.bookmarketplace.entity.User;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    Transaction getTransactionById(Long id);
    Transaction addTransaction(Transaction newTransaction);
    Transaction updateTransaction(Transaction newTransaction, Long id);
    void deleteTransaction(Long id);


}
