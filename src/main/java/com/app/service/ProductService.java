package com.app.service;

import com.app.entity.Product;
import com.app.entity.ProductImage;
import com.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // CREATE PRODUCT
public Product saveProduct(Product product, List<MultipartFile> images) {

    try {
        List<ProductImage> imageList = new ArrayList<>();

        if (images != null) {
            for (MultipartFile file : images) {

                if (file == null || file.isEmpty()) continue;

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                File uploadPath = new File(uploadDir);

                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                File dest = new File(uploadPath, fileName);
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
        throw new RuntimeException("Error saving product");
    }
}

    // ✅ THIS MUST BE OUTSIDE ABOVE METHOD
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}