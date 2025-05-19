package com.example.A_Task3.dtos;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BorrowingTransactionRequest {
    private UUID bookId;
    private UUID borrowerId;
}