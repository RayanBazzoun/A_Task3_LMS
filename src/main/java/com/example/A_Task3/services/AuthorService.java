package com.example.A_Task3.services;

import com.example.A_Task3.dtos.CreateAuthorRequest;
import com.example.A_Task3.dtos.CreateBookRequest;
import com.example.A_Task3.dtos.UpdateAuthorRequest;
import com.example.A_Task3.models.Author;
import com.example.A_Task3.models.Book;
import com.example.A_Task3.repositories.IAuthorRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    @Autowired
    private IAuthorRepository authorRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Author createAuthor(CreateAuthorRequest request) {
        Author author = modelMapper.map(request, Author.class);
        return authorRepository.save(author);
    }

    public Author getAuthorById(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author updateAuthor(UUID id, UpdateAuthorRequest request) {
        Author author = getAuthorById(id);
        if (request.getName() != null) {
            author.setName(request.getName());
        }
        if (request.getBiography() != null) {
            author.setBiography(request.getBiography());
        }
        return authorRepository.save(author);
    }

    public void deleteAuthor(UUID id) {
        authorRepository.deleteById(id);
    }

    public List<String> getBooksByAuthor(UUID id) {
        Author author = getAuthorById(id);
        return author.getBooks().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
}
