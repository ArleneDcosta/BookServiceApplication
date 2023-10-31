package com.books.bookmarketplace.controller;

import com.books.bookmarketplace.entity.Book;
import com.books.bookmarketplace.entity.Transaction;
import com.books.bookmarketplace.errorhandler.ValidationException;
import com.books.bookmarketplace.service.BookService;
import com.books.bookmarketplace.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

@RestController
@RequestMapping("/transactions")
public class TransactionController {


    private final TransactionService transactionService;
    private static final Logger logger = Logger.getLogger(BookController.class.getName());

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(transactions);
    }


    @GetMapping("/getTransactionById")
    public ResponseEntity<Transaction> getTransactionById(@RequestParam(name = "transactionId") @Positive Long transactionId) {
        if (transactionId == null || transactionId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid Transaction ID. Please provide a positive numeric value."));
        }
        logger.log(Level.INFO, "Fetching Transaction with ID: " + transactionId);
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction != null) {
            logger.log(Level.INFO, "Retrieved Transaction: " + transactionId);
            return ResponseEntity.ok().body(transaction);
        } else {
            logger.log(Level.WARNING, "Transaction with ID " + transactionId + " not found.");
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addTransaction")
    public ResponseEntity<Transaction> addBook(@Valid @RequestBody Transaction transaction, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            throw new ValidationException(errors);
        }
        try {
            Transaction savedTransaction = transactionService.addTransaction(transaction);
            logger.log(Level.INFO, "Successfully added new Transaction: " );
            return ResponseEntity.ok().body(savedTransaction);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while adding a book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateTransaction")
    public ResponseEntity<Transaction> updateTransaction(@RequestParam(name = "transactionId") @Positive Long transactionId, @Valid @RequestBody Transaction transaction, BindingResult bindingResult) {
        if (transactionId == null || transactionId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid Transaction ID. Please provide a positive numeric value."));
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            throw new ValidationException(errors);
        }
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(transaction, transactionId);
            logger.log(Level.INFO, "Updated the book: " + transactionId);
            return ResponseEntity.ok().body(updatedTransaction);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while updating a book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteTransaction")
    public ResponseEntity<String> deleteTransaction(@RequestParam(name = "transactionId") Long transactionId) {
        if (transactionId == null || transactionId <= 0) {
            throw new ValidationException(Collections.singletonList("Invalid Transaction ID. Please provide a positive numeric value."));
        }
        try {
            logger.log(Level.INFO, "Deleting transaction with ID: " + transactionId);
            transactionService.deleteTransaction(transactionId);
            logger.log(Level.INFO, "Transaction with ID " + transactionId + " deleted successfully.");
            return ResponseEntity.ok("Transaction with ID " + transactionId + " deleted successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while deleting the transaction with ID " + transactionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting the transaction: " + e.getMessage());
        }
    }
}