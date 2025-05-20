package com.eshop.productservice.controller;

import com.eshop.productservice.dto.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-service")
public interface CategoryClient {
    @GetMapping("/api/categories/{id}")
    CategoryResponse getCategoryById(@PathVariable("id") Long id);
}