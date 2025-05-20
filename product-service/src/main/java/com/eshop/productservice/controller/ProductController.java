package com.eshop.productservice.controller;

import com.eshop.productservice.dto.CreateProductRequest;
import com.eshop.productservice.dto.ProductWithCategoryDTO;
import com.eshop.productservice.entity.Product;
import com.eshop.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductWithCategoryDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductWithCategory(id));
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        Product createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductWithCategoryDTO>> getAllProducts() {
        List<ProductWithCategoryDTO> products = productService.getAllProductsWithCategories();
        return ResponseEntity.ok(products);
    }

}
