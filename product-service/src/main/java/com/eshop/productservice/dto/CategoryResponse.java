package com.eshop.productservice.dto;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String nom;
    private String description;
}
