package com.app.service;

import com.app.entity.Product;
import com.app.entity.ProductImage;
import com.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // ✅ FIXED upload path (use application.properties value)
    private final String uploadDir = "/workspaces/productbackend/uploads/";

    // =========================
    // ✅ CREATE PRODUCT
    // =========================
    public Product saveProduct(Product product, List<MultipartFile> images) {

        try {
            List<ProductImage> imageList = new ArrayList<>();

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if (images != null) {
                for (MultipartFile file : images) {

                    if (file == null || file.isEmpty()) continue;

                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                    File dest = new File(uploadDir + fileName);
                    file.transferTo(dest);

                    ProductImage img = new ProductImage();
                    img.setImageUrl("/uploads/" + fileName);
                    img.setProduct(product);

                    imageList.add(img);
                }
            }

            product.setImages(imageList);

            return productRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving product: " + e.getMessage());
        }
    }

    // =========================
    // ✅ DELETE PRODUCT
    // =========================
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    // =========================
    // ✅ UPDATE PRODUCT (FIXED)
    // =========================
    public Product updateProduct(int id,
                                 String name,
                                 String description,
                                 double price,
                                 String category,
                                 int stock,
                                 List<MultipartFile> images) {

        try {

            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // ✅ update fields
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setStock(stock);

            // ✅ IMPORTANT FIX: handle images safely
            if (images != null && !images.isEmpty()) {

                // ❗ clear old images (JPA handles delete because orphanRemoval=true)
                product.getImages().clear();

                List<ProductImage> newImages = new ArrayList<>();

                for (MultipartFile file : images) {

                    if (file == null || file.isEmpty()) continue;

                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                    File dest = new File(uploadDir + fileName);
                    file.transferTo(dest);

                    ProductImage img = new ProductImage();
                    img.setImageUrl("/uploads/" + fileName);
                    img.setProduct(product);

                    newImages.add(img);
                }

                product.getImages().addAll(newImages);
            }

            return productRepository.save(product);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

    // =========================
    // ✅ GET ALL
    // =========================
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}