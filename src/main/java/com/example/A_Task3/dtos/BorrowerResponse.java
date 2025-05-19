package com.example.A_Task3.dtos;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BorrowerResponse {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
}