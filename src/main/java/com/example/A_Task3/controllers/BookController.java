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
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody CreateBookRequest request) {
        log.info("Request to create book: {}", request);
        try {
            Book book = bookService.addBook(request);
            BookResponse response = modelMapper.map(book, BookResponse.class);
            response.setAuthorNames(
                    book.getAuthors().stream()
                            .map(Author::getName)
                            .collect(Collectors.toList())
            );
            log.info("Book created successfully: id={}", book.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.error("Failed to create book: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Unexpected error while creating book", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable UUID id) {
        log.info("Request to get book with id={}", id);
        try {
            Book book = bookService.getBookById(id);
            BookResponse response = modelMapper.map(book, BookResponse.class);
            log.info("Book retrieved: id={}", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.warn("Book not found with id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving book with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String author) {
        log.info("Searching books - title: {}, category: {}, author: {}", title, category, author);
        try {
            List<Book> books = bookService.searchBooks(title, category, author);
            List<BookResponse> responses = books.stream()
                    .map(book -> modelMapper.map(book, BookResponse.class))
                    .collect(Collectors.toList());
            log.info("Search found {} books", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception ex) {
            log.error("Error while searching books", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id, @RequestBody UpdateBookRequest request) {
        log.info("Request to update book with id={}, request={}", id, request);
        try {
            Book updated = bookService.updateBook(id, request);
            BookResponse response = modelMapper.map(updated, BookResponse.class);
            log.info("Book updated successfully: id={}", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            log.warn("Book update failed for id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while updating book with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        log.info("Request to delete book with id={}", id);
        try {
            bookService.deleteBook(id);
            log.info("Book deleted: id={}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            log.warn("Failed to delete book with id={}: {}", id, ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unexpected error while deleting book with id={}", id, ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}