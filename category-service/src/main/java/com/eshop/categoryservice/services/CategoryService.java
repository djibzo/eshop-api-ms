package com.eshop.categoryservice.services;

import com.eshop.categoryservice.entity.Category;
import com.eshop.categoryservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Category create(Category category) {
        return repository.save(category);
    }
    public Category getCategoryById(Long id){
        return repository.findById(id).orElse(null);
    }
}
