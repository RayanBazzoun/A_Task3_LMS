package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.BorrowingTransactionResponse;
import com.example.A_Task3.dtos.BorrowingTransactionRequest;
import com.example.A_Task3.services.BorrowingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class BorrowingTransactionController {
    @Autowired
    private BorrowingTransactionService transactionService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingTransactionResponse> borrowBook(@RequestBody BorrowingTransactionRequest request) {
        return ResponseEntity.ok(transactionService.borrowBook(request));
    }

    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowingTransactionResponse> returnBook(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(transactionService.returnBook(transactionId));
    }

    @GetMapping
    public ResponseEntity<List<BorrowingTransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingTransactionResponse> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }
}