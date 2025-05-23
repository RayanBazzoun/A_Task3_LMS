package com.example.A_Task3.services;

import com.example.A_Task3.dtos.CreateAuthorRequest;
import com.example.A_Task3.dtos.UpdateAuthorRequest;
import com.example.A_Task3.models.Author;
import com.example.A_Task3.models.Book;
import com.example.A_Task3.repositories.IAuthorRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthorService {
    @Autowired
    private IAuthorRepository authorRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Author createAuthor(CreateAuthorRequest request) {
        log.debug("Creating author with request: {}", request);
        try {
            Author author = modelMapper.map(request, Author.class);
            Author saved = authorRepository.save(author);
            log.info("Author created: id={}", saved.getId());
            return saved;
        } catch (Exception ex) {
            log.error("Failed to create author: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to create author", ex);
        }
    }

    public Author getAuthorById(UUID id) {
        log.debug("Getting author by id={}", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Author not found with id={}", id);
                    return new RuntimeException("Author not found with id: " + id);
                });
    }

    public List<Author> getAllAuthors() {
        log.debug("Getting all authors");
        List<Author> authors = authorRepository.findAll();
        log.info("Found {} authors", authors.size());
        return authors;
    }

    public Author updateAuthor(UUID id, UpdateAuthorRequest request) {
        log.debug("Updating author id={}, request={}", id, request);
        try {
            Author author = getAuthorById(id);
            if (request.getName() != null) {
                author.setName(request.getName());
            }
            if (request.getBiography() != null) {
                author.setBiography(request.getBiography());
            }
            Author updated = authorRepository.save(author);
            log.info("Author updated: id={}", updated.getId());
            return updated;
        } catch (RuntimeException ex) {
            log.warn("Failed to update author with id={}: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while updating author with id={}", id, ex);
            throw new RuntimeException("Failed to update author", ex);
        }
    }

    public void deleteAuthor(UUID id) {
        log.debug("Deleting author id={}", id);
        try {
            authorRepository.deleteById(id);
            log.info("Author deleted: id={}", id);
        } catch (RuntimeException ex) {
            log.warn("Failed to delete author with id={}: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while deleting author with id={}", id, ex);
            throw new RuntimeException("Failed to delete author", ex);
        }
    }

    public List<String> getBooksByAuthor(UUID id) {
        log.debug("Getting books by author id={}", id);
        Author author = getAuthorById(id);
        if (author == null) {
            log.warn("Author not found with id={}", id);
            throw new RuntimeException("Author not found with id: " + id);
        }
        List<String> books = author.getBooks().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
        log.info("Found {} books for author id={}", books.size(), id);
        return books;
    }
}