package com.example.A_Task3.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingTransactionResponse {
    private UUID transactionId;
    private boolean success;
    private String message;
    private BigDecimal totalPrice;
}