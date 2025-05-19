package com.example.A_Task3.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "author")
@EqualsAndHashCode(exclude = "books")
public class Author {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String biography;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();
}
