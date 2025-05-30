package com.example.A_Task3.services;

import com.example.A_Task3.client.EmailClient;
import com.example.A_Task3.client.EmailRequest;
import com.example.A_Task3.dtos.BorrowingTransactionRequest;
import com.example.A_Task3.models.*;
import com.example.A_Task3.models.enums.BorrowStatus;
import com.example.A_Task3.repositories.IBookRepository;
import com.example.A_Task3.repositories.IBorrowerRepository;
import com.example.A_Task3.repositories.IBorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class BorrowingTransactionService {
    @Autowired
    private IBorrowingTransactionRepository transactionRepository;
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IBorrowerRepository borrowerRepository;
    @Autowired
    private EmailClient emailClient;
    @Value("${borrower.transaction.limit}")
    private int transactionLimit;

    public BorrowingTransaction borrowBook(BorrowingTransactionRequest request) {
        log.debug("Borrowing book: {}", request);

        int count = transactionRepository.countByBorrowerIdAndStatus(request.getBorrowerId(), BorrowStatus.BORROWED);
        if (count >= transactionLimit) {
            log.warn("Borrower {} has reached the maximum allowed transactions ({}).", request.getBorrowerId(), transactionLimit);
            throw new RuntimeException("Borrower has reached the maximum allowed transactions.");
        }
        try {
            Book book = bookRepository.findById(request.getBookId())
                    .orElseThrow(() -> {
                        log.warn("Book not found: id={}", request.getBookId());
                        return new RuntimeException("Book not found.");
                    });
            if (!book.isAvailability()) {
                log.warn("Book with id={} is not available for borrowing", book.getId());
                throw new RuntimeException("Book is not available for borrowing.");
            }
            Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                    .orElseThrow(() -> {
                        log.warn("Borrower not found: id={}", request.getBorrowerId());
                        return new RuntimeException("Borrower not found.");
                    });
            book.setAvailability(false);
            bookRepository.save(book);
            BorrowingTransaction transaction = BorrowingTransaction.builder()
                    .book(book)
                    .borrower(borrower)
                    .borrowDate(LocalDate.now())
                    .status(BorrowStatus.BORROWED)
                    .build();
            BorrowingTransaction saved = transactionRepository.save(transaction);
            log.info("Book borrowed successfully: transactionId={}", saved.getId());
            String message = "Book " + book.getTitle() + " borrowed successfully";
            emailClient.sendEmail(new EmailRequest(borrower.getEmail(), message));
            return saved;
        } catch (RuntimeException ex) {
            log.warn("Failed to borrow book: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error in borrowBook: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to borrow book", ex);
        }
    }


    public BorrowingTransaction returnBook(UUID transactionId) {
        log.debug("Returning book for transactionId={}", transactionId);
        try {
            BorrowingTransaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> {
                        log.warn("Transaction not found: id={}", transactionId);
                        return new RuntimeException("Transaction not found.");
                    });
            if (transaction.getStatus() == BorrowStatus.RETURNED) {
                log.warn("Book has already been returned for transactionId={}", transactionId);
                throw new RuntimeException("Book has already been returned.");
            }
            transaction.setStatus(BorrowStatus.RETURNED);
            transaction.setReturnDate(LocalDate.now());
            Book book = transaction.getBook();
            book.setAvailability(true);
            bookRepository.save(book);
            BorrowingTransaction saved = transactionRepository.save(transaction);
            log.info("Book returned: transactionId={}", transactionId);
            return saved;
        } catch (RuntimeException ex) {
            log.warn("Failed to return book for transactionId={}: {}", transactionId, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error in returnBook for transactionId={}: {}", transactionId, ex);
            throw new RuntimeException("Failed to return book", ex);
        }
    }
}