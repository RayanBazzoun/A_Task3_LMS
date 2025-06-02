package com.example.A_Task3.models;

import com.example.A_Task3.models.enums.Category;
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
@Table(name="book")
@EqualsAndHashCode(exclude = "authors")
@ToString(exclude = "authors")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String isbn;
    private String title;
    @Enumerated(EnumType.STRING)
    private Category category;
    private boolean availability;
    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
    @Column(columnDefinition = "TEXT")
    private String properties; // stores JSON string with pricing rules

}
