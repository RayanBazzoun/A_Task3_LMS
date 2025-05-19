package com.example.A_Task3.services;

import com.example.A_Task3.dtos.CreateBookRequest;
import com.example.A_Task3.dtos.UpdateBookRequest;
import com.example.A_Task3.models.Author;
import com.example.A_Task3.models.Book;
import com.example.A_Task3.repositories.IAuthorRepository;
import com.example.A_Task3.repositories.IBookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IAuthorRepository authorRepository;
    @Autowired
    private ModelMapper modelMapper;

    private String generateValidIsbn13() {
        String prefix = "978";
        String body = String.format("%09d", new Random().nextInt(1_000_000_000));
        String raw = prefix + body;

        int sum = 0;
        for (int i = 0; i < raw.length(); i++) {
            int digit = Character.getNumericValue(raw.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return new String(raw + checkDigit);
    }

    public Book addBook(CreateBookRequest request) {
        List<Author> authors = request.getAuthorNames().stream()
                .map(name -> authorRepository.findByName(name)
                        .orElseThrow(() -> new RuntimeException("Author not found: " + name)))
                .collect(Collectors.toList());

        Book book = modelMapper.map(request, Book.class);
        book.setAuthors(new HashSet<>(authors));
        book.setIsbn(generateValidIsbn13());
        book.setAvailability(true);

        return bookRepository.save(book);
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public List<Book> searchBooks(String title, String category, String authorName) {
        List<Book> books = bookRepository.findAll();
        if (title != null) {
            books = books.stream()
                    .filter(book -> book.getTitle() != null && book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (category != null) {
            books = books.stream()
                    .filter(book -> book.getCategory() != null && book.getCategory().name().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        if (authorName != null) {
            books = books.stream()
                    .filter(book -> book.getAuthors().stream()
                            .anyMatch(author -> author.getName().equalsIgnoreCase(authorName)))
                    .collect(Collectors.toList());
        }
        return books;
    }

    public Book updateBook(UUID id, UpdateBookRequest request) {
        Book book = getBookById(id);
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getCategory() != null) {
            book.setCategory(request.getCategory());
        }
        if (request.getAuthorNames() != null && !request.getAuthorNames().isEmpty()) {
            List<Author> authors = request.getAuthorNames().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseThrow(() -> new RuntimeException("Author not found: " + name)))
                    .collect(Collectors.toList());
            book.setAuthors(new HashSet<>(authors));
        }
        if (request.getAvailability() != null) {
            book.setAvailability(request.getAvailability());
        }
        return bookRepository.save(book);
    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
