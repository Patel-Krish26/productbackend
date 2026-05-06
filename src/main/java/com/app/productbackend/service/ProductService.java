package com.app.productbackend.service;

import com.app.productbackend.entity.Product;
import com.app.productbackend.entity.ProductImage;
import com.app.productbackend.repository.ProductRepository;
import com.app.productbackend.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    private final String uploadDir =
            System.getProperty("user.dir") + "/uploads/";

    // ================= GET =================
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProductsWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // ================= FILTER =================
    public Page<Product> filterProducts(String keyword, String category, Pageable pageable) {

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        if (hasKeyword && hasCategory)
            return productRepository
                    .findByNameContainingIgnoreCaseAndCategoryIgnoreCase(keyword, category, pageable);

        if (hasKeyword)
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);

        if (hasCategory)
            return productRepository.findByCategoryIgnoreCase(category, pageable);

        return productRepository.findAll(pageable);
    }

    // ================= CREATE =================
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
            product.setCreatedAt(LocalDateTime.now());

            return productRepository.save(product);

        } catch (Exception e) {
            throw new RuntimeException("Save failed: " + e.getMessage());
        }
    }

    // ================= UPDATE (🔥 FIXED) =================
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
                    .orElseThrow(() -> new RuntimeException("Not found"));

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setStock(stock);

            // 🔥 IF NEW IMAGES → DELETE OLD FIRST
            if (images != null && !images.isEmpty()) {

                // get old images
                List<ProductImage> oldImages = imageRepository.findAll()
                        .stream()
                        .filter(img -> img.getProduct().getId() == id)
                        .toList();

                // delete files from disk
                for (ProductImage img : oldImages) {
                    try {
                        File file = new File(System.getProperty("user.dir") + img.getImageUrl());
                        if (file.exists()) file.delete();
                    } catch (Exception ignored) {}
                }

                // delete from DB
                imageRepository.deleteByProductId(id);

                // save new images
                List<ProductImage> newImages = new ArrayList<>();

                for (MultipartFile file : images) {

                    if (file == null || file.isEmpty()) continue;

                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    file.transferTo(new File(uploadDir + fileName));

                    ProductImage img = new ProductImage();
                    img.setImageUrl("/uploads/" + fileName);
                    img.setProduct(product);

                    newImages.add(img);
                }

                product.setImages(newImages);
            }

            return productRepository.save(product);

        } catch (Exception e) {
            throw new RuntimeException("Update failed: " + e.getMessage());
        }
    }

    // ================= DELETE =================
    public void deleteProduct(int id) {

        // delete images from disk
        List<ProductImage> images = imageRepository.findAll()
                .stream()
                .filter(img -> img.getProduct().getId() == id)
                .toList();

        for (ProductImage img : images) {
            try {
                File file = new File(System.getProperty("user.dir") + img.getImageUrl());
                if (file.exists()) file.delete();
            } catch (Exception ignored) {}
        }

        productRepository.deleteById(id);
    }
}