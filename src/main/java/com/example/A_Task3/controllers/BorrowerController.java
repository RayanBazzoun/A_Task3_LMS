package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.BorrowerRequest;
import com.example.A_Task3.dtos.BorrowerResponse;
import com.example.A_Task3.models.Borrower;
import com.example.A_Task3.services.BorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/borrowers")
public class BorrowerController {
    @Autowired
    private BorrowerService borrowerService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BorrowerResponse> addBorrower(@RequestBody BorrowerRequest request) {
        log.info("Request to add borrower: {}", request);
        try {
            Borrower borrower = borrowerService.addBorrower(request);
            log.info("Borrower added successfully: id={}", borrower.getId());
            return ResponseEntity.ok(modelMapper.map(borrower, BorrowerResponse.class));
        } catch (RuntimeException ex) {
            log.error("Failed to add borrower: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Unexpected error while adding borrower", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponse> getBorrower(@PathVariable UUID id) {
        log.info("Request to get borrower with id={}", id);
        try {
            Borrower borrower = borrowerService.getBorrowerById(id);
            log.info("Borrower retrieved: id={}", id);
            return ResponseEntity.ok(modelMapper.map(borrower, BorrowerResponse.class));
        } catch (RuntimeException ex) {
            log.warn("Borrower not found with id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving borrower with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers() {
        log.info("Request to get all borrowers");
        try {
            List<BorrowerResponse> borrowers = borrowerService.getAllBorrowers().stream()
                    .map(b -> modelMapper.map(b, BorrowerResponse.class))
                    .collect(Collectors.toList());
            log.info("Found {} borrowers", borrowers.size());
            return ResponseEntity.ok(borrowers);
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving all borrowers", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowerResponse> updateBorrower(@PathVariable UUID id, @RequestBody BorrowerRequest request) {
        log.info("Request to update borrower id={}, request={}", id, request);
        try {
            Borrower borrower = borrowerService.updateBorrower(id, request);
            log.info("Borrower updated: id={}", id);
            return ResponseEntity.ok(modelMapper.map(borrower, BorrowerResponse.class));
        } catch (RuntimeException ex) {
            log.warn("Failed to update borrower with id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while updating borrower with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable UUID id) {
        log.info("Request to delete borrower with id={}", id);
        try {
            borrowerService.deleteBorrower(id);
            log.info("Borrower deleted: id={}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            log.warn("Failed to delete borrower with id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while deleting borrower with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}