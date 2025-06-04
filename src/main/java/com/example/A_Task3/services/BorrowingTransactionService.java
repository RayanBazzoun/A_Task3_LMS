package com.example.A_Task3.services;

import com.example.A_Task3.client.CmsClient;
import com.example.A_Task3.client.EmailClient;
import com.example.A_Task3.client.dtos.EmailRequest;
import com.example.A_Task3.client.dtos.TransactionRequest;
import com.example.A_Task3.client.dtos.TransactionResponse;
import com.example.A_Task3.client.enums.CurrencyType;
import com.example.A_Task3.client.enums.TransactionType;
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
import java.time.temporal.ChronoUnit;
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

            PricingProperties pricing = objectMapper.readValue(book.getProperties(), PricingProperties.class);

            BigDecimal basePrice = book.getPrice();
            BigDecimal insuranceFee = pricing.getInsurance_fees();
            BigDecimal extraDayPrice = pricing.getExtra_days_rental_price();

            LocalDate borrowDate = LocalDate.now();
            LocalDate returnDate = request.getReturnDate();

            long days = ChronoUnit.DAYS.between(borrowDate, returnDate);
            long extraDays = Math.max(days - 7, 0);
            BigDecimal extraDaysCharge = extraDayPrice.multiply(BigDecimal.valueOf(extraDays));

            BigDecimal total = basePrice.add(extraDaysCharge).add(insuranceFee);

            TransactionRequest cmsRequest = TransactionRequest.builder()
                    .cardNumber(request.getCardNumber())
                    .transactionAmount(total)
                    .currency(request.getCurrency())
                    .transactionType(TransactionType.DEBIT)
                    .build();

            log.info("Sending CMS request: {}", cmsRequest);
            TransactionResponse cmsResponse = cmsClient.createTransaction(cmsRequest);

            if (cmsResponse == null) {
                log.warn("CMS returned null response.");
                throw new RuntimeException("CMS service did not respond.");
            }

            if (!cmsResponse.isSuccess()) {
                log.warn("CMS payment failed. Details: success={}, msg={}, response={}",
                        cmsResponse.isSuccess(),
                        cmsResponse.getMessage(),
                        cmsResponse);
                throw new RuntimeException("Payment failed: " + cmsResponse.getMessage());
            }


            book.setAvailability(false);
            bookRepository.save(book);

            BorrowingTransaction transaction = BorrowingTransaction.builder()
                    .book(book)
                    .borrower(borrower)
                    .borrowDate(borrowDate)
                    .returnDate(returnDate)
                    .status(BorrowStatus.BORROWED)
                    .cardNumber(request.getCardNumber())
                    .totalPrice(total)
                    .insuranceFee(insuranceFee)
                    .extraDaysCharge(extraDaysCharge)
                    .currency(request.getCurrency())
                    .build();



            BorrowingTransaction saved = transactionRepository.save(transaction);

            emailClient.sendEmail(new EmailRequest(borrower.getEmail(), "Book " + book.getTitle() + " borrowed successfully."));

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

            PricingProperties pricing = objectMapper.readValue(book.getProperties(), PricingProperties.class);
            LocalDate dueDate = transaction.getBorrowDate().plusWeeks(1);
            boolean returnedOnTime = !transaction.getReturnDate().isAfter(dueDate);

            if (returnedOnTime) {
                BigDecimal refund = pricing.getInsurance_fees();
                TransactionRequest refundRequest = TransactionRequest.builder()
                        .cardNumber(transaction.getCardNumber())
                        .transactionAmount(refund)
                        .currency(transaction.getCurrency())
                        .transactionType(TransactionType.CREDIT)
                        .build();
                TransactionResponse refundResponse = cmsClient.createTransaction(refundRequest);
                if (refundResponse == null || !refundResponse.isSuccess()) {
                    log.warn("CMS refund failed: {}", refundResponse != null ? refundResponse.getMessage() : "CMS unavailable");
                }
            }
            Borrower borrower=transaction.getBorrower();
            emailClient.sendEmail(new EmailRequest(borrower.getEmail(), "Book " + book.getTitle() + " borrowed successfully."));
            return saved;
        } catch (Exception ex) {
            log.error("Failed to return book: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to return book", ex);
        }
    }
}