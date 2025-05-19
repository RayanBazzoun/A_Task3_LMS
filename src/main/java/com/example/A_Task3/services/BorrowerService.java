package com.example.A_Task3.services;

import com.example.A_Task3.dtos.BorrowerRequest;
import com.example.A_Task3.models.Borrower;
import com.example.A_Task3.repositories.IBorrowerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BorrowerService {
    @Autowired
    private IBorrowerRepository borrowerRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Borrower addBorrower(BorrowerRequest request) {
        Borrower borrower = modelMapper.map(request, Borrower.class);
        return borrowerRepository.save(borrower);
    }

    public Borrower getBorrowerById(UUID id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found: " + id));
    }

    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    public Borrower updateBorrower(UUID id, BorrowerRequest request) {
        Borrower borrower = getBorrowerById(id);
        if (request.getName() != null) borrower.setName(request.getName());
        if (request.getEmail() != null) borrower.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) borrower.setPhoneNumber(request.getPhoneNumber());
        return borrowerRepository.save(borrower);
    }

    public void deleteBorrower(UUID id) {
        borrowerRepository.deleteById(id);
    }
}