package com.example.A_Task3.client.dtos;

public class EmailRequest {
    private String email;
    private String message;
    public EmailRequest() {}
    public EmailRequest(String email, String message) { this.email = email; this.message = message; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
