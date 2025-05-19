package com.example.A_Task3.repositories;

import com.example.A_Task3.models.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IBorrowerRepository extends JpaRepository<Borrower, UUID> {
}