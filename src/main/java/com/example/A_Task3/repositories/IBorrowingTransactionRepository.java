package com.example.A_Task3.repositories;

import com.example.A_Task3.models.BorrowingTransaction;
import com.example.A_Task3.models.enums.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IBorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, UUID> {
    int countByBorrowerIdAndStatus(UUID borrowerId, BorrowStatus borrowStatus);
}