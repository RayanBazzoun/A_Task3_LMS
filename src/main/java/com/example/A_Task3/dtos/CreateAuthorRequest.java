package com.example.A_Task3.dtos;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateAuthorRequest {
    @NotNull(message="name can't be null")
    private String name;
    private String biography;
}
