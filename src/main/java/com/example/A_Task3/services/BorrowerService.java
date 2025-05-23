package com.example.A_Task3.services;

import com.example.A_Task3.dtos.BorrowerRequest;
import com.example.A_Task3.models.Borrower;
import com.example.A_Task3.repositories.IBorrowerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BorrowerService {
    @Autowired
    private IBorrowerRepository borrowerRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Borrower addBorrower(BorrowerRequest request) {
        log.debug("Adding borrower: {}", request);
        try {
            Borrower borrower = modelMapper.map(request, Borrower.class);
            Borrower saved = borrowerRepository.save(borrower);
            log.info("Borrower added: id={}", saved.getId());
            return saved;
        } catch (Exception ex) {
            log.error("Failed to add borrower: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to add borrower", ex);
        }
    }

    public Borrower getBorrowerById(UUID id) {
        log.debug("Getting borrower by id={}", id);
        return borrowerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Borrower not found with id={}", id);
                    return new RuntimeException("Borrower not found: " + id);
                });
    }

    public List<Borrower> getAllBorrowers() {
        log.debug("Getting all borrowers");
        List<Borrower> borrowers = borrowerRepository.findAll();
        log.info("Found {} borrowers", borrowers.size());
        return borrowers;
    }

    public Borrower updateBorrower(UUID id, BorrowerRequest request) {
        log.debug("Updating borrower id={}, request={}", id, request);
        try {
            Borrower borrower = getBorrowerById(id);
            if (request.getName() != null) borrower.setName(request.getName());
            if (request.getEmail() != null) borrower.setEmail(request.getEmail());
            if (request.getPhoneNumber() != null) borrower.setPhoneNumber(request.getPhoneNumber());
            Borrower updated = borrowerRepository.save(borrower);
            log.info("Borrower updated: id={}", updated.getId());
            return updated;
        } catch (RuntimeException ex) {
            log.warn("Failed to update borrower with id={}: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while updating borrower with id={}", id, ex);
            throw new RuntimeException("Failed to update borrower", ex);
        }
    }

    public void deleteBorrower(UUID id) {
        log.debug("Deleting borrower id={}", id);
        try {
            borrowerRepository.deleteById(id);
            log.info("Borrower deleted: id={}", id);
        } catch (RuntimeException ex) {
            log.warn("Failed to delete borrower with id={}: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while deleting borrower with id={}", id, ex);
            throw new RuntimeException("Failed to delete borrower", ex);
        }
    }
}