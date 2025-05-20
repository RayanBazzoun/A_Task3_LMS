package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.BorrowingTransactionRequest;
import com.example.A_Task3.models.BorrowingTransaction;
import com.example.A_Task3.services.BorrowingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class BorrowingTransactionController {
    @Autowired
    private BorrowingTransactionService transactionService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransaction> borrowBook(@RequestBody BorrowingTransactionRequest request) {
        BorrowingTransaction transaction = transactionService.borrowBook(request);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowingTransaction> returnBook(@PathVariable UUID transactionId) {
        BorrowingTransaction transaction = transactionService.returnBook(transactionId);
        return ResponseEntity.ok(transaction);
    }
}