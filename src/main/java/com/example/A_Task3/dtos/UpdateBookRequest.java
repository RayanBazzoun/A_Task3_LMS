package com.example.A_Task3.dtos;
import com.example.A_Task3.models.enums.Category;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateBookRequest {
    private String title;
    private Category category;
    private List<String> authorNames;
    private Boolean availability;
}