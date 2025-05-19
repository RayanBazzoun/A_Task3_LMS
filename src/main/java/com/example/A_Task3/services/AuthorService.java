package com.example.A_Task3.services;

import com.example.A_Task3.dtos.CreateAuthorRequest;
import com.example.A_Task3.dtos.CreateBookRequest;
import com.example.A_Task3.models.Author;
import com.example.A_Task3.repositories.IAuthorRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    private IAuthorRepository authorRepository;
    @Autowired
    private ModelMapper modelMapper;
    public Author createAuthor( CreateAuthorRequest request){
        Author author = modelMapper.map(request,Author.class);
        return authorRepository.save(author);
    }
}
