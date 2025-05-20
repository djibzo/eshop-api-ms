package com.eshop.productservice.services;

import com.eshop.productservice.controller.CategoryClient;
import com.eshop.productservice.dto.CategoryResponse;
import com.eshop.productservice.dto.CreateProductRequest;
import com.eshop.productservice.dto.ProductWithCategoryDTO;
import com.eshop.productservice.entity.Product;
import com.eshop.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryClient categoryClient;

    public ProductService(ProductRepository productRepository,CategoryClient categoryClient) {
        this.productRepository = productRepository;
        this.categoryClient=categoryClient;
    }

    public ProductWithCategoryDTO getProductWithCategory(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CategoryResponse category = categoryClient.getCategoryById(product.getCategoryId());

        return ProductWithCategoryDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(category)
                .build();
    }

    public Product createProduct(CreateProductRequest request) {
        CategoryResponse category = categoryClient.getCategoryById(request.getCategoryId());
        if (category == null) {
            throw new RuntimeException("Catégorie non trouvée avec l'ID : " + request.getCategoryId());
        }
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategoryId(request.getCategoryId());

        return productRepository.save(product);
    }

    public List<ProductWithCategoryDTO> getAllProductsWithCategories() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            // Appel au microservice category pour chaque produit
            CategoryResponse category = categoryClient.getCategoryById(product.getCategoryId());

            return ProductWithCategoryDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .category(category)
                    .build();
        }).collect(Collectors.toList());
    }

}
