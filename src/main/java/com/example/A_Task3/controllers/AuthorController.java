package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.AuthorResponse;
import com.example.A_Task3.dtos.CreateAuthorRequest;
import com.example.A_Task3.dtos.UpdateAuthorRequest;
import com.example.A_Task3.models.Author;
import com.example.A_Task3.services.AuthorService;
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
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody CreateAuthorRequest request) {
        log.info("Request to create author: {}", request);
        try {
            Author author = authorService.createAuthor(request);
            AuthorResponse response = modelMapper.map(author, AuthorResponse.class);
            log.info("Author created successfully: id={}", author.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.error("Failed to create author: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Unexpected error while creating author", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable UUID id) {
        log.info("Request to get author with id={}", id);
        try {
            Author author = authorService.getAuthorById(id);
            AuthorResponse response = modelMapper.map(author, AuthorResponse.class);
            log.info("Author retrieved: id={}", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.warn("Author not found with id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving author with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        log.info("Request to get all authors");
        try {
            List<Author> authors = authorService.getAllAuthors();
            List<AuthorResponse> responses = authors.stream()
                    .map(author -> modelMapper.map(author, AuthorResponse.class))
                    .collect(Collectors.toList());
            log.info("Found {} authors", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving all authors", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable UUID id, @RequestBody UpdateAuthorRequest request) {
        log.info("Request to update author with id={}, request={}", id, request);
        try {
            Author updated = authorService.updateAuthor(id, request);
            AuthorResponse response = modelMapper.map(updated, AuthorResponse.class);
            log.info("Author updated successfully: id={}", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.warn("Author update failed for id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while updating author with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        log.info("Request to delete author with id={}", id);
        try {
            authorService.deleteAuthor(id);
            log.info("Author deleted: id={}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            log.warn("Failed to delete author with id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while deleting author with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<String>> getBooksByAuthor(@PathVariable UUID id) {
        log.info("Request to get books by author id={}", id);
        try {
            List<String> bookTitles = authorService.getBooksByAuthor(id);
            log.info("Found {} books for author id={}", bookTitles.size(), id);
            return ResponseEntity.ok(bookTitles);
        } catch (RuntimeException ex) {
            log.warn("Failed to find books for author id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while finding books for author id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}