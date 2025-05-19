package com.example.A_Task3.repositories;

import com.example.A_Task3.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IAuthorRepository extends JpaRepository<Author, UUID> {
    Optional<Author> findByName(String name);
    List<Author> findAllByNameIn(List<String> names);
}
