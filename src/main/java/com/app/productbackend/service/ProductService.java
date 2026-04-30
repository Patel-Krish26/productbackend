package com.app.service;

import com.app.entity.Product;
import com.app.entity.ProductImage;
import com.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final String uploadDir = "/workspaces/productbackend/uploads/";

    // =========================
    // 🔥 FILTER + PAGINATION (FIXED)
    // =========================
    public Page<Product> filterProducts(String keyword, String category, Pageable pageable) {

        String nameFilter = (keyword == null) ? "" : keyword;
        String categoryFilter = (category == null) ? "" : category;

        Page<Product> page = productRepository
                .findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                        nameFilter,
                        categoryFilter,
                        pageable
                );

        // ✅ FIX: if requested page > total pages → return empty page safely
        if (pageable.getPageNumber() >= page.getTotalPages() && page.getTotalPages() > 0) {
            Pageable newPage = PageRequest.of(page.getTotalPages() - 1, pageable.getPageSize());
            return productRepository
                    .findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                            nameFilter,
                            categoryFilter,
                            newPage
                    );
        }

        return page;
    }

    // =========================
    // ✅ PAGINATION ONLY
    // =========================
    public Page<Product> getProductsWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

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
            throw new RuntimeException("Error saving product: " + e.getMessage());
        }
    }

    // =========================
    // DELETE
    // =========================
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    // =========================
    // UPDATE
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

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setStock(stock);

            if (images != null && !images.isEmpty()) {

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
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

    // =========================
    // SEARCH
    // =========================
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // =========================
    // CATEGORY
    // =========================
    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    // =========================
    // GET ALL
    // =========================
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}