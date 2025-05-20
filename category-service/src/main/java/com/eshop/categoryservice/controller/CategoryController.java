package com.eshop.categoryservice.controller;

import com.eshop.categoryservice.entity.Category;
import com.eshop.categoryservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping
    public List<Category> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        System.out.println("NOM: " + category.getNom());
        System.out.println("DESCRIPTION: " + category.getDescription());
        return service.create(category);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Category category = service.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
}