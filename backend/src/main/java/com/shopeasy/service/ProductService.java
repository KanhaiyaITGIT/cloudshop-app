package com.shopeasy.service;

import com.shopeasy.dto.ProductRequest;
import com.shopeasy.exception.ResourceNotFoundException;
import com.shopeasy.model.Product;
import com.shopeasy.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Product not found with id: " + id));
    }

    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}