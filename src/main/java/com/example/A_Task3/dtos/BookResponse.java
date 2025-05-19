package com.example.A_Task3.dtos;
import java.util.List;
import java.util.UUID;
import com.example.A_Task3.models.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BookResponse {
    private UUID id;
    private String title;
    private String isbn;
    private Category category;
    private List<String> authorNames;
    private boolean availability;
}
