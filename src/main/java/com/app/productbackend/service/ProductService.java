package com.app.productbackend.service;

import com.app.productbackend.entity.Product;
import com.app.productbackend.entity.ProductImage;
import com.app.productbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // =========================
    // UPLOAD DIR
    // =========================
    private final String uploadDir =
            System.getProperty("user.dir") + "/uploads/";

    // =========================
    // GET ALL
    // =========================
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // =========================
    // PAGINATION
    // =========================
    public Page<Product> getProductsWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // =========================
    // FILTER
    // =========================
    public Page<Product> filterProducts(String keyword, String category, Pageable pageable) {

        String nameFilter = (keyword == null) ? "" : keyword;
        String categoryFilter = (category == null) ? "" : category;

        return productRepository
                .findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                        nameFilter,
                        categoryFilter,
                        pageable
                );
    }

    // =========================
    // CREATE PRODUCT
    // =========================
    public Product saveProduct(Product product, List<MultipartFile> images) {

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            List<ProductImage> imageList = new ArrayList<>();

            if (images != null) {
                for (MultipartFile file : images) {

                    if (file == null || file.isEmpty()) continue;

                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                    file.transferTo(new File(uploadDir + fileName));

                    ProductImage img = new ProductImage();
                    img.setImageUrl("/uploads/" + fileName);
                    img.setProduct(product);

                    imageList.add(img);
                }
            }

            product.setImages(imageList);

            return productRepository.save(product);

        } catch (Exception e) {
            throw new RuntimeException("Product save failed: " + e.getMessage());
        }
    }

    // =========================
    // UPDATE PRODUCT
    // =========================
    public Product updateProduct(
            int id,
            String name,
            String description,
            double price,
            String category,
            int stock,
            List<MultipartFile> images
    ) {

        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setStock(stock);

            if (images != null && !images.isEmpty()) {

                List<ProductImage> imageList = new ArrayList<>();

                for (MultipartFile file : images) {

                    if (file == null || file.isEmpty()) continue;

                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                    file.transferTo(new File(uploadDir + fileName));

                    ProductImage img = new ProductImage();
                    img.setImageUrl("/uploads/" + fileName);
                    img.setProduct(product);

                    imageList.add(img);
                }

                product.setImages(imageList);
            }

            return productRepository.save(product);

        } catch (Exception e) {
            throw new RuntimeException("Update failed: " + e.getMessage());
        }
    }

    // =========================
    // DELETE
    // =========================
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    // =========================
    // SEARCH
    // =========================
    public List<Product> searchProducts(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll();
        }

        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // =========================
    // CATEGORY
    // =========================
    public List<Product> getByCategory(String category) {

        if (category == null || category.isBlank()) {
            return productRepository.findAll();
        }

        return productRepository.findByCategoryIgnoreCase(category);
    }
}