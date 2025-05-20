package com.example.A_Task3.services;

import com.example.A_Task3.dtos.BorrowingTransactionRequest;
import com.example.A_Task3.models.*;
import com.example.A_Task3.models.enums.BorrowStatus;
import com.example.A_Task3.repositories.IBookRepository;
import com.example.A_Task3.repositories.IBorrowerRepository;
import com.example.A_Task3.repositories.IBorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BorrowingTransactionService {
    @Autowired
    private IBorrowingTransactionRepository transactionRepository;
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IBorrowerRepository borrowerRepository;

    public BorrowingTransaction borrowBook(BorrowingTransactionRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found."));
        if (!book.isAvailability()) {
            throw new RuntimeException("Book is not available for borrowing.");
        }
        Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found."));
        book.setAvailability(false);
        bookRepository.save(book);
        BorrowingTransaction transaction = BorrowingTransaction.builder()
                .book(book)
                .borrower(borrower)
                .borrowDate(LocalDate.now())
                .status(BorrowStatus.BORROWED)
                .build();
        return transactionRepository.save(transaction);
    }

    public BorrowingTransaction returnBook(UUID transactionId) {
        BorrowingTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found."));
        if (transaction.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book has already been returned.");
        }
        transaction.setStatus(BorrowStatus.RETURNED);
        transaction.setReturnDate(LocalDate.now());
        Book book = transaction.getBook();
        book.setAvailability(true);
        bookRepository.save(book);
        return transactionRepository.save(transaction);
    }
}