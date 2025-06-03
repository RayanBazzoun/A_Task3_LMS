package com.example.A_Task3.models;

import com.example.A_Task3.models.enums.BorrowStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrowing_transaction")
public class BorrowingTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Borrower borrower;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    // New fields
    private String cardNumber;

    private BigDecimal totalPrice;
    private BigDecimal insuranceFee;
    private BigDecimal extraDaysCharge;
}