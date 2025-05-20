package com.eshop.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}