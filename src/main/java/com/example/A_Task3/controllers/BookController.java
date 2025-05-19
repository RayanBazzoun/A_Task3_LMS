package com.example.A_Task3.controllers;


import com.example.A_Task3.dtos.BookResponse;
import com.example.A_Task3.dtos.CreateBookRequest;
import com.example.A_Task3.models.Book;
import com.example.A_Task3.services.BookService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody CreateBookRequest request){
        Book book= bookService.addBook(request);
        BookResponse response= modelMapper.map(book,BookResponse.class);
        System.out.println("DEBUG Author Names: " + response.getAuthorNames());
        return ResponseEntity.ok(response);
    }
}
