package com.example.A_Task3;

import com.example.A_Task3.dtos.BookResponse;
import com.example.A_Task3.models.Book;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping: authors -> authorNames (just name, not toString)
        modelMapper.typeMap(Book.class, BookResponse.class)
                .addMappings(mapper -> mapper.map(
                        src -> {
                            System.out.println("DEBUG: Mapping authors for book " + src.getTitle());
                            return src.getAuthors() == null ? Collections.emptyList() :
                                    src.getAuthors().stream().map(a -> a.getName()).toList();
                        },
                        BookResponse::setAuthorNames
                ));

        return modelMapper;
    }
}