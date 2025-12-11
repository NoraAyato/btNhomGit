package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import com.example.demo.model.Category;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ICategoryRepository;
import com.example.demo.repository.IProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(String name, double price, String image, Long categoryId) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setImage(image);
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
            product.setCategory(category);
            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm sản phẩm : " + e.getMessage());
        }
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void updateProduct(Long id, String name, double price, String image, Long categoryId) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
            product.setName(name);
            product.setPrice(price);
            if (image != null && !image.isEmpty()) {
                product.setImage(image);
            }
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
            product.setCategory(category);
            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }

    }
}
