package com.app.productbackend.controller;

import com.app.productbackend.entity.Product;
import com.app.productbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // =========================
    // PAGINATION
    // =========================
    @GetMapping("/paged")
    public Page<Product> getPaged(Pageable pageable) {
        return productService.getProductsWithPagination(pageable);
    }

    // =========================
    // FILTER
    // =========================
    @GetMapping("/filter")
    public Page<Product> filter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Pageable pageable
    ) {
        return productService.filterProducts(keyword, category, pageable);
    }

    // =========================
    // CREATE PRODUCT (ADMIN)
    // =========================
    @PostMapping(value = "/admin/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam int stock,
            @RequestParam(required = false) List<MultipartFile> images
    ) {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStock(stock);

        Product saved = productService.saveProduct(product, images);

        return ResponseEntity.ok(saved);
    }

    // =========================
    // UPDATE PRODUCT (ADMIN)
    // =========================
    @PutMapping(value = "/admin/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam int stock,
            @RequestParam(required = false) List<MultipartFile> images
    ) {

        Product updated = productService.updateProduct(
                id, name, description, price, category, stock, images
        );

        return ResponseEntity.ok(updated);
    }

    // =========================
    // DELETE PRODUCT
    // =========================
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // =========================
    // SEARCH
    // =========================
    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    // =========================
    // CATEGORY
    // =========================
    @GetMapping("/category")
    public List<Product> byCategory(@RequestParam String category) {
        return productService.getByCategory(category);
    }
}