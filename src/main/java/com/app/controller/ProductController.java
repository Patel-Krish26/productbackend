package com.app.controller;

import com.app.entity.Product;
import com.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://cuddly-adventure-g4r5446p64qwcvvxq-5173.app.github.dev"
})
public class ProductController {

    @Autowired
    private ProductService productService;

    // ===========================
    // ✅ GET ALL PRODUCTS
    // ===========================
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // ===========================
    // ✅ CREATE PRODUCT (MULTI IMAGE)
    // ===========================
    @PostMapping(consumes = "multipart/form-data")
    public Product createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam int stock,
            @RequestParam("images") List<MultipartFile> images
    ) {

        Product product = new Product();

        // Setting product data
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStock(stock);

        return productService.saveProduct(product, images);
    }
}