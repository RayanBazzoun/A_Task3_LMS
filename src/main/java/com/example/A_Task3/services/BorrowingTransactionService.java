package com.example.A_Task3.services;

import com.example.A_Task3.client.CmsClient;
import com.example.A_Task3.client.EmailClient;
import com.example.A_Task3.client.EmailRequest;
import com.example.A_Task3.client.TransactionRequest;
import com.example.A_Task3.dtos.BorrowingTransactionRequest;
import com.example.A_Task3.models.*;
import com.example.A_Task3.models.enums.BorrowStatus;
import com.example.A_Task3.repositories.IBookRepository;
import com.example.A_Task3.repositories.IBorrowerRepository;
import com.example.A_Task3.repositories.IBorrowingTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    private CmsClient cmsClient;
    @Autowired
    private ObjectMapper objectMapper;

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
                    .orElseThrow(() -> new RuntimeException("Book not found."));
            if (!book.isAvailability()) {
                throw new RuntimeException("Book is not available for borrowing.");
            }

            Borrower borrower = borrowerRepository.findById(request.getBorrowerId())
                    .orElseThrow(() -> new RuntimeException("Borrower not found."));

            // Parse pricing properties
            PricingProperties pricing = objectMapper.readValue(book.getProperties(), PricingProperties.class);

            BigDecimal basePrice = BigDecimal.valueOf(10); // Example base price
            BigDecimal insuranceFee = pricing.getInsurance_fees();
            BigDecimal total = basePrice.add(insuranceFee);

            // Charge the borrower
            cmsClient.createTransaction(TransactionRequest.builder()
                    .cardId(borrower.getCardId())
                    .transactionAmount(total)
                    .transactionType("DEBIT")
                    .build());

            book.setAvailability(false);
            bookRepository.save(book);

            BorrowingTransaction transaction = BorrowingTransaction.builder()
                    .book(book)
                    .borrower(borrower)
                    .borrowDate(LocalDate.now())
                    .status(BorrowStatus.BORROWED)
                    .build();

            BorrowingTransaction saved = transactionRepository.save(transaction);
            emailClient.sendEmail(new EmailRequest(borrower.getEmail(), "Book " + book.getTitle() + " borrowed successfully"));
            return saved;

        } catch (Exception ex) {
            log.error("Failed to borrow book: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to borrow book", ex);
        }
    }

    public BorrowingTransaction returnBook(UUID transactionId) {
        log.debug("Returning book for transactionId={}", transactionId);
        try {
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
            BorrowingTransaction saved = transactionRepository.save(transaction);

            // Parse pricing
            PricingProperties pricing = objectMapper.readValue(book.getProperties(), PricingProperties.class);
            LocalDate dueDate = transaction.getBorrowDate().plusWeeks(1);
            boolean returnedOnTime = !transaction.getReturnDate().isAfter(dueDate);

            if (returnedOnTime) {
                BigDecimal refund = pricing.getInsurance_fees();
                cmsClient.createTransaction(TransactionRequest.builder()
                        .cardId(transaction.getBorrower().getCardId())
                        .transactionAmount(refund)
                        .transactionType("CREDIT")
                        .build());
            }

            return saved;
        } catch (Exception ex) {
            log.error("Failed to return book: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to return book", ex);
        }
    }
}
