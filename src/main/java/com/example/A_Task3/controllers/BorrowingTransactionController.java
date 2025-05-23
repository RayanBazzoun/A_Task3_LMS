package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.BorrowingTransactionRequest;
import com.example.A_Task3.models.BorrowingTransaction;
import com.example.A_Task3.services.BorrowingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/transactions")
public class BorrowingTransactionController {
    @Autowired
    private BorrowingTransactionService transactionService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransaction> borrowBook(@RequestBody BorrowingTransactionRequest request) {
        log.info("Request to borrow book: {}", request);
        try {
            BorrowingTransaction transaction = transactionService.borrowBook(request);
            log.info("Book borrowed successfully: transactionId={}", transaction.getId());
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException ex) {
            log.warn("Failed to borrow book: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Unexpected error while borrowing book", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowingTransaction> returnBook(@PathVariable UUID transactionId) {
        log.info("Request to return book for transactionId={}", transactionId);
        try {
            BorrowingTransaction transaction = transactionService.returnBook(transactionId);
            log.info("Book returned successfully: transactionId={}", transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException ex) {
            log.warn("Failed to return book for transactionId={}: {}", transactionId, ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Unexpected error while returning book for transactionId={}", transactionId, ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}