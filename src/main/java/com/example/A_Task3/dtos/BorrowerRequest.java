package com.example.A_Task3.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BorrowerRequest {
    private String name;
    private String email;
    private String phoneNumber;
}