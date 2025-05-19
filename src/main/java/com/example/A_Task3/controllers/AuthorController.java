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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody CreateAuthorRequest request) {
        Author author = authorService.createAuthor(request);
        AuthorResponse response = modelMapper.map(author, AuthorResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable UUID id) {
        Author author = authorService.getAuthorById(id);
        AuthorResponse response = modelMapper.map(author, AuthorResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        List<AuthorResponse> responses = authors.stream()
                .map(author -> modelMapper.map(author, AuthorResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable UUID id, @RequestBody UpdateAuthorRequest request) {
        Author updated = authorService.updateAuthor(id, request);
        AuthorResponse response = modelMapper.map(updated, AuthorResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<String>> getBooksByAuthor(@PathVariable UUID id) {
        List<String> bookTitles = authorService.getBooksByAuthor(id);
        return ResponseEntity.ok(bookTitles);
    }
}