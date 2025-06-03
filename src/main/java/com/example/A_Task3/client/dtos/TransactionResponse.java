package com.example.A_Task3.client.dtos;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private boolean success;
    private String message;
    private String transactionId;
    private String status;
}
