package com.app.productbackend.controller;

import com.app.productbackend.entity.Product;
import com.app.productbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ===========================
    // ✅ GET ALL
    // ===========================
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // ===========================
    // ✅ PAGINATION
    // ===========================
    @GetMapping("/paged")
    public Page<Product> getProductsPaged(Pageable pageable) {
        return productService.getProductsWithPagination(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    // 🔍 SEARCH
    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    // 📦 CATEGORY
    @GetMapping("/category")
    public List<Product> getByCategory(@RequestParam String name) {
        return productService.getByCategory(name);
    }

    // 🔥 FILTER + PAGINATION
    @GetMapping("/filter")
    public Page<Product> filterProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Pageable pageable
    ) {
        return productService.filterProducts(keyword, category, pageable);
    }

    // ===========================
    // ✅ UPDATE
    // ===========================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam int stock,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {

        Product updatedProduct = productService.updateProduct(
                id, name, description, price, category, stock, images
        );

        return ResponseEntity.ok(updatedProduct);
    }

    // ===========================
    // ✅ CREATE
    // ===========================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam int stock,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {

        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStock(stock);

        if (images == null) {
            return productService.saveProduct(product, List.of());
        }

        return productService.saveProduct(product, images);
    }
}