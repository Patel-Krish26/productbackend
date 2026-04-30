package com.app.controller;

import com.app.entity.Product;
import com.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/products")
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


    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }


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
    // ✅ CREATE PRODUCT (MULTI IMAGE)
    // ===========================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("category") String category,
            @RequestParam("stock") int stock,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {

        Product product = new Product();

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStock(stock);

        // Handle null images safely
        if (images == null) {
            return productService.saveProduct(product, List.of());
        }

        return productService.saveProduct(product, images);
    }
}