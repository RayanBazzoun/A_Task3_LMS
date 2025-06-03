package com.example.A_Task3.dtos;

import com.example.A_Task3.client.enums.CurrencyType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingTransactionRequest {
    private UUID bookId;
    private UUID borrowerId;
    private String cardNumber;
    private LocalDate returnDate;
    private CurrencyType currency;
}