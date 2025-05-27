package com.eshop.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String role;
    private String username;

    public RegisterRequest(String email, String role, String username) {
        this.email = email;
        this.role = role;
        this.username = username;
    }
}
