package com.example.A_Task3.dtos;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateAuthorRequest {
    private String name;
    private String biography;
}