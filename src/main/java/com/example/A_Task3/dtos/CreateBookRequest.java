package com.example.A_Task3.dtos;

import com.example.A_Task3.models.enums.Category;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.NotFound;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateBookRequest {
    @NotNull(message = "You need a isbn")
    private String isbn;
    @NotNull(message = "you need a category")
    private Category category;
}