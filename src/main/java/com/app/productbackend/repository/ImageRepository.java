package com.app.productbackend.repository;

import com.app.productbackend.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Integer> {

    // ✅ REQUIRED for update
    void deleteByProductId(int productId);
}