package com.example.A_Task3.repositories;

import com.example.A_Task3.models.Author;
import com.example.A_Task3.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IBookRepository extends JpaRepository<Book, UUID> {

}
