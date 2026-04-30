package com.app.repository;

import com.app.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ProductImage, Integer> {

    // ✅ REQUIRED for update
    void deleteByProductId(int productId);
}