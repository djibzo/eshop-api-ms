package com.eshop.categoryservice.repository;

import com.eshop.categoryservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
