package com.app.productbackend.service;

import com.app.productbackend.entity.Product;
import com.app.productbackend.entity.ProductImage;
import com.app.productbackend.repository.ImageRepository;
import com.app.productbackend.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ IMPORTANT
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional // ✅🔥 CRITICAL FIX (solves your 500 error)
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    // =========================
    // GET ALL
    // =========================
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProductsWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> filterProducts(String keyword, String category, Pageable pageable) {

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        if (hasKeyword && hasCategory) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(keyword, category, pageable);
        }

        if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        if (hasCategory) {
            return productRepository.findByCategoryIgnoreCase(category, pageable);
        }

        return productRepository.findAll(pageable);
    }

    // =========================
    // CREATE
    // =========================
    public Product saveProduct(
            String name,
            String description,
            double price,
            String category,
            int stock,
            List<MultipartFile> images
    ) {

        try {
            Product product = new Product();

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setStock(stock);
            product.setCreatedAt(LocalDateTime.now());

            product = productRepository.save(product);

            saveImages(product, images);

            return product;

        } catch (Exception e) {
            throw new RuntimeException("Create failed: " + e.getMessage());
        }
    }

    // =========================
    // UPDATE
    // =========================
    public Product updateProduct(
            int id,
            String name,
            String description,
            double price,
            String category,
            int stock,
            List<MultipartFile> images,
            boolean replaceImages
    ) {

        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setStock(stock);

            // ✅ DELETE OLD IMAGES (NOW WORKS — transaction active)
            if (replaceImages) {
                imageRepository.deleteByProductId(id);
            }

            // ✅ SAVE NEW IMAGES
            if (images != null && !images.isEmpty()) {
                saveImages(product, images);
            }

            return productRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Update failed: " + e.getMessage());
        }
    }

    // =========================
    // IMAGE SAVE
    // =========================
    private void saveImages(Product product, List<MultipartFile> images) throws Exception {

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

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

    // =========================
    // DELETE
    // =========================
    public void deleteProduct(int id) {
        imageRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }
}