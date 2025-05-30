package com.example.A_Task3.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-service", url = "http://localhost:8081")
public interface EmailClient {
    @PostMapping("/send-email")
    void sendEmail(@RequestBody EmailRequest request);
}