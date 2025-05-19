package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.BorrowerRequest;
import com.example.A_Task3.dtos.BorrowerResponse;
import com.example.A_Task3.models.Borrower;
import com.example.A_Task3.services.BorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/borrowers")
public class BorrowerController {
    @Autowired
    private BorrowerService borrowerService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BorrowerResponse> addBorrower(@RequestBody BorrowerRequest request) {
        Borrower borrower = borrowerService.addBorrower(request);
        return ResponseEntity.ok(modelMapper.map(borrower, BorrowerResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponse> getBorrower(@PathVariable UUID id) {
        Borrower borrower = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(modelMapper.map(borrower, BorrowerResponse.class));
    }

    @GetMapping
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers() {
        List<BorrowerResponse> borrowers = borrowerService.getAllBorrowers().stream()
                .map(b -> modelMapper.map(b, BorrowerResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(borrowers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowerResponse> updateBorrower(@PathVariable UUID id, @RequestBody BorrowerRequest request) {
        Borrower borrower = borrowerService.updateBorrower(id, request);
        return ResponseEntity.ok(modelMapper.map(borrower, BorrowerResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable UUID id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }
}