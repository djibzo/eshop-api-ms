package com.eshop.productservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductWithCategoryDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private CategoryResponse category;
}
