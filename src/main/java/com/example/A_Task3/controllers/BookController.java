package com.example.A_Task3.controllers;

import com.example.A_Task3.dtos.BookResponse;
import com.example.A_Task3.dtos.CreateBookRequest;
import com.example.A_Task3.dtos.UpdateBookRequest;
import com.example.A_Task3.models.Book;
import com.example.A_Task3.models.Author;
import com.example.A_Task3.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody CreateBookRequest request) {
        Book book = bookService.addBook(request);
        BookResponse response = modelMapper.map(book, BookResponse.class);
        response.setAuthorNames(
                book.getAuthors().stream()
                        .map(Author::getName)
                        .collect(Collectors.toList())
        );        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable UUID id) {
        Book book = bookService.getBookById(id);
        BookResponse response = modelMapper.map(book, BookResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String author) {
        List<Book> books = bookService.searchBooks(title, category, author);
        List<BookResponse> responses = books.stream()
                .map(book -> modelMapper.map(book, BookResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id, @RequestBody UpdateBookRequest request) {
        Book updated = bookService.updateBook(id, request);
        BookResponse response = modelMapper.map(updated, BookResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}