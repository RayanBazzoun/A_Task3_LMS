package com.example.A_Task3.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private UUID cardId;
    private BigDecimal transactionAmount;
    private String transactionType;
    private String currency = "USD";
}
