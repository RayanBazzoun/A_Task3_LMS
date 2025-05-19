package com.example.A_Task3.dtos;

import com.example.A_Task3.models.enums.BorrowStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BorrowingTransactionResponse {
    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private UUID borrowerId;
    private String borrowerName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private BorrowStatus status;
}