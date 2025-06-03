package com.example.A_Task3.client.dtos;

import com.example.A_Task3.client.enums.CurrencyType;
import com.example.A_Task3.client.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String cardNumber;
    private BigDecimal transactionAmount;
    private TransactionType transactionType;
    private CurrencyType currency;
}
