package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.AuthorResponse;
import com.example.A_Task3.dtos.CreateAuthorRequest;
import com.example.A_Task3.models.Author;
import com.example.A_Task3.services.AuthorService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody CreateAuthorRequest request) {
        Author author = authorService.createAuthor(request);
        AuthorResponse response = modelMapper.map(author, AuthorResponse.class);
        return ResponseEntity.ok(response);
    }

}
